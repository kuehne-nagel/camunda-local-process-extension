package com.kn.bpa.testapplication.lex.controller;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/lex-tasktype-command")
@Transactional
public class LexTaskTypeComandController {

  private static final String START_SUFFIX = "_start";
  private static final String END_EVENT_SUFFIX = "_end";
  private static final String BPMN_SUFFIX = ".bpmn";
  private static final String LEX_PREFIX = "LEX_";

  private final RepositoryService repositoryService;

    public LexTaskTypeComandController(final RepositoryService repositoryService) {
      this.repositoryService = repositoryService;
    }

    @RequestMapping(value = "/createLexTaskType", method = RequestMethod.GET)
    public ResponseEntity<String> createLexTask(
            @ApiParam(required = true, defaultValue = "ArrangeCustomsDE") //
            @RequestParam(value = "taskTypeId") final String taskTypeId,
            @ApiParam(required = true, defaultValue = "Arrange Customs for DE") //
            @RequestParam(required = false, value = "taskTypeName") String taskTypeName) {

      final String processDefinitionKey = LEX_PREFIX + taskTypeId;
      final BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(processDefinitionKey)
        .name(processDefinitionKey)
        .startEvent(taskTypeId + START_SUFFIX).name(taskTypeName + " Started")
        .userTask(taskTypeId).name(taskTypeName)
        .endEvent(taskTypeId + END_EVENT_SUFFIX)
        .camundaExecutionListenerDelegateExpression(ExecutionListener.EVENTNAME_END,
          "${lexTaskCompletedDelegate}")
        .name(taskTypeName + " Ended")
        .messageEventDefinition()
        .messageEventDefinitionDone()
        .done();

      repositoryService
        .createDeployment()
        .addModelInstance(taskTypeId + BPMN_SUFFIX, modelInstance)
        .deploy();

      return ResponseEntity.ok("taskType with id " + taskTypeId + " created!");
      }
}
