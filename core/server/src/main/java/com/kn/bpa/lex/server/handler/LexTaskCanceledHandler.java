package com.kn.bpa.lex.server.handler;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_TASK_ID;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.communication.events.LexTaskCanceledEvent;
import com.kn.bpa.lex.server.delegate.EvaluateLexTaskProcess;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LexTaskCanceledHandler implements Consumer<LexTaskCanceledEvent> {

  private final RuntimeService runtimeService;
  private final RepositoryService repositoryService;

  @Autowired
  public LexTaskCanceledHandler(final RuntimeService runtimeService, final RepositoryService repositoryService) {
    this.runtimeService = runtimeService;
    this.repositoryService = repositoryService;
  }

  @Override
  public void accept(final LexTaskCanceledEvent lexTaskCanceledEvent) {
    final List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
      .processInstanceBusinessKey(lexTaskCanceledEvent.getBusinessObjectIdentifier())
      .list();

    final String processDefinitionId = getProcessDefinition(lexTaskCanceledEvent.getTaskTypeId(),
      processInstances.stream()
        .map(ProcessInstance::getProcessDefinitionId)
        .collect(Collectors.toList())).getId();

    final List<ProcessInstance> processInstancesFilteredByDefinitionId =
      processInstances.stream().filter(instance -> instance.getProcessDefinitionId().equals(processDefinitionId)).collect(Collectors.toList());

    final Map<String, Map<String, String>> processVariables = getProcessVariables(processInstancesFilteredByDefinitionId);

    final Predicate<ProcessInstance> processInstanceHasLexClientApplicationId = instance -> Optional.ofNullable(processVariables.get(instance.getId()))
      .map(variables -> variables.get(LEX_CLIENT_APPLICATION_ID))
      .filter(lexTaskCanceledEvent.getClientApplicationId()::equals)
      .isPresent();

    final Predicate<ProcessInstance> processInstanceHasLexClientId = instance -> Optional.ofNullable(processVariables.get(instance.getId()))
      .map(variables -> variables.get(LEX_CLIENT_TASK_ID))
      .filter(lexTaskCanceledEvent.getClientTaskId()::equals)
      .isPresent();

    final List<ProcessInstance> matchingProcesses = processInstancesFilteredByDefinitionId.stream()
      .filter(processInstanceHasLexClientApplicationId)
      .filter(processInstanceHasLexClientId)
      .collect(Collectors.toList());

    if (matchingProcesses.size() != 1) {
      log.error("Can't cancel lex task. Expecting 1 Task, but found {} matching tasks for event: {}", matchingProcesses.size(), lexTaskCanceledEvent);
      return;
    }

    runtimeService.deleteProcessInstance(matchingProcesses.get(0).getProcessInstanceId(), lexTaskCanceledEvent.getCancellationReason(), false, true);
  }

  private ProcessDefinition getProcessDefinition(final String taskTypeId, final List<String> processDefinitionIds) {
    return repositoryService.createProcessDefinitionQuery()
      .processDefinitionIdIn(processDefinitionIds.toArray(new String[0]))
      .processDefinitionKey(EvaluateLexTaskProcess.lexTaskProcessDefinitionKey(taskTypeId))
      .singleResult();
  }

  Map<String, Map<String, String>> getProcessVariables(final List<ProcessInstance> processInstances) {
    return runtimeService.createVariableInstanceQuery()
      .processInstanceIdIn(processInstances.stream().map(ProcessInstance::getProcessInstanceId).toArray(String[]::new))
      .variableNameIn(LEX_CLIENT_TASK_ID, LEX_CLIENT_APPLICATION_ID)
      .list().stream()
      .collect(Collectors
        .groupingBy(VariableInstance::getProcessInstanceId, Collectors.toMap(VariableInstance::getName, var -> (String) var.getValue())));
  }
}

