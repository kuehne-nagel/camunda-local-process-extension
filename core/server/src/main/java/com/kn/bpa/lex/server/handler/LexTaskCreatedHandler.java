package com.kn.bpa.lex.server.handler;

import java.util.function.Consumer;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.common.LexConstants.MESSAGE;
import com.kn.bpa.lex.common.LexConstants.VARIABLE;
import com.kn.bpa.lex.communication.events.LexTaskCreatedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumes a {@link LexTaskCreatedEvent} and notifies the event-based sub-process of the bridge instance.
 * This will start a one-user-task process that acts as a proxy for the remote task.
 */
@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
public class LexTaskCreatedHandler implements Consumer<LexTaskCreatedEvent> {

  private final RuntimeService runtimeService;

  @Autowired
  public LexTaskCreatedHandler(final RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  @Override
  public void accept(final LexTaskCreatedEvent lexTaskCreatedEvent) {
    log.info("Starting lex task process via sending {} to bridge", lexTaskCreatedEvent, lexTaskCreatedEvent.getBridgeProcessId());

    final VariableMap variables = Variables.createVariables()
      .putValueTyped(VARIABLE.LEX_TASK_TYPE_ID, Variables.stringValue(lexTaskCreatedEvent.getTaskTypeId(), true))
      .putValueTyped(VARIABLE.LEX_CLIENT_APPLICATION_ID, Variables.stringValue(lexTaskCreatedEvent.getClientApplicationId(), true))
      .putValueTyped(VARIABLE.LEX_CLIENT_TASK_ID, Variables.stringValue(lexTaskCreatedEvent.getClientTaskId(), true));

    runtimeService.createMessageCorrelation(MESSAGE.START_LEX_TASK_PROCESS)
      .processInstanceId(lexTaskCreatedEvent.getBridgeProcessId())
      .setVariables(variables)
      .correlate();
  }
}
