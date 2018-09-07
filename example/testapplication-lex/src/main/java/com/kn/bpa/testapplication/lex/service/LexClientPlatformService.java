package com.kn.bpa.testapplication.lex.service;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.BRIDGE_PROCESS_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;
import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.JMS_PROPERTY_COMMAND_TYPE;
import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.QUEUE_PLATFORM_TO_LEX;
import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.SELECTOR_LEX_PROCESS_CANCELED;
import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.SELECTOR_LEX_PROCESS_STARTED;
import static com.kn.bpa.testapplication.jms.DockerJmsAutoConfiguration.SELECTOR_LEX_TASK_COMPLETED;

import java.util.List;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.kn.bpa.lex.client.LexClientService;
import com.kn.bpa.lex.communication.commands.CancelLexProcessCommand;
import com.kn.bpa.lex.communication.commands.CompleteLexTaskCommand;
import com.kn.bpa.lex.communication.commands.StartLexProcessCommand;
import com.kn.bpa.lex.communication.events.LexProcessCanceledEvent;
import com.kn.bpa.lex.communication.events.LexProcessCompletedEvent;
import com.kn.bpa.lex.communication.events.LexTaskCanceledEvent;
import com.kn.bpa.lex.communication.events.LexTaskCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LexClientPlatformService implements LexClientService {

  private final RestTemplate restTemplate;
  private final RuntimeService runtimeService;
  private final TaskService taskService;

  @Value("${lex.server.url}")
  String lexServerUrl = "localhost";

  @Autowired
  public LexClientPlatformService(
    final RestTemplate restTemplate,
    final RuntimeService runtimeService,
    final TaskService taskService) {
    this.restTemplate = restTemplate;
    this.runtimeService = runtimeService;
    this.taskService = taskService;
  }

  /**
   * Helper method which takes the jms body as json string and maps it into an StartLexProcessCommand
   *
   * @param command ... The StartLexProcessCommand in json format
   */
  @JmsListener(destination = QUEUE_PLATFORM_TO_LEX, selector = JMS_PROPERTY_COMMAND_TYPE + "='" + SELECTOR_LEX_PROCESS_STARTED + "'")
  public void handleStartLexProcess(final String command) {
    this.handleStartLexProcess(StartLexProcessCommand.fromJson(command));
  }

  @Override
  public void handleStartLexProcess(final StartLexProcessCommand command) {
    log.info("Start lex process: {}", command);

    final VariableMap variables = new VariableMapImpl();
    variables.put(BRIDGE_PROCESS_ID, command.getLexBridgeProcessInstanceId());
    variables.put(LEX_CLIENT_APPLICATION_ID, command.getClientApplicationId());

    runtimeService.startProcessInstanceByKey(
      command.getTaskTypeId(),
      command.getBusinessObjectIdentifier(),
      variables);
  }

  /**
   * Helper method which takes the jms body as json string and maps it into an CompleteLexTaskCommand
   *
   * @param command ... The CompleteLexTaskCommand in json format
   */
  @JmsListener(destination = QUEUE_PLATFORM_TO_LEX, selector = JMS_PROPERTY_COMMAND_TYPE + "='" + SELECTOR_LEX_TASK_COMPLETED + "'")
  public void handleLexTaskCompleted(final String command) {
    this.handleLexTaskCompleted(CompleteLexTaskCommand.fromJson(command));
  }

  @Override
  public void handleLexTaskCompleted(final CompleteLexTaskCommand command) {
    log.info("Complete lex task: {}", command);

    taskService.complete(command.getTaskId());
  }

  /**
   * Helper method which takes the jms body as json string and maps it into an CancelLexProcessCommand
   *
   * @param command ... The CancelLexProcessCommand in json format
   */
  @JmsListener(destination = QUEUE_PLATFORM_TO_LEX, selector = JMS_PROPERTY_COMMAND_TYPE + "='" + SELECTOR_LEX_PROCESS_CANCELED + "'")
  public void handleLexProcessCancel(final String command) {
    this.handleLexProcessCancellation(CancelLexProcessCommand.fromJson(command));
  }

  @Override
  public void handleLexProcessCancellation(final CancelLexProcessCommand command) {
    log.info("Cancel lex process: {}", command);

    final List<String> processIds = runtimeService.createProcessInstanceQuery()
      .processInstanceBusinessKey(command.getBusinessObjectIdentifier())
      .list()
      .stream()
      .map(ProcessInstance::getProcessInstanceId)
      .collect(Collectors.toList());

    final boolean skipCustomListeners = true;
    final boolean externallyTerminated = true;

    runtimeService.deleteProcessInstances(processIds, command.getCancellationReason(), skipCustomListeners, externallyTerminated);
  }

  /**
   * Converts the LexTaskCreatedEvent into Json and puts it to the jms queue of <code>DockerJmsAutoConfiguration.QUEUE_LEX_TO_PLATFORM_TASK</code>
   */
  @Override
  public void notifyLexTaskCreated(final LexTaskCreatedEvent event) {
    log.info("Lex task created: {} -> Notify lex server", event);
    restTemplate.put("/lex/task/created", event.toJson());
  }

  @Override
  public void notifyLexTaskCanceled(final LexTaskCanceledEvent event) {
    log.info("Lex task canceled: {} -> Notify lex server", event);
    restTemplate.put("/lex/task/canceled", event.toJson());
  }

  @Override
  public void notifyLexProcessCanceled(final LexProcessCanceledEvent event) {
    log.info("Lex process canceled: {} -> Notify lex server", event);
    restTemplate.put("/lex/process/canceled", event.toJson());
  }

  /**
   * Converts the LexProcessCompletedEvent into Json and puts it to the jms queue of <code>DockerJmsAutoConfiguration.QUEUE_LEX_TO_PLATFORM</code>
   */
  @Override
  public void notifyLexProcessCompleted(final LexProcessCompletedEvent event) {
    log.info("Lex process completed: {} -> Notify lex server", event);
    restTemplate.put("/lex/process/completed", event.toJson());
  }
}
