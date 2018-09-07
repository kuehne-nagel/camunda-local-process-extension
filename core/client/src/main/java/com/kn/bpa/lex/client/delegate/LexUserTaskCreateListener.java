package com.kn.bpa.lex.client.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.BRIDGE_PROCESS_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.client.LexClientService;
import com.kn.bpa.lex.communication.events.LexTaskCreatedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Global taskCreate listener that uses {@link LexClientService#notifyLexTaskCreated(LexTaskCreatedEvent)} to publish a {@link LexTaskCreatedEvent}.
 * <p>
 * This can be used in every LEX task as an on create listener.
 */
@Component
@Slf4j
public class LexUserTaskCreateListener implements TaskListener {

  private final LexClientService lexClientService;

  @Autowired
  public LexUserTaskCreateListener(final LexClientService lexClientService) {
    this.lexClientService = lexClientService;
  }

  @Override public void notify(final DelegateTask delegateTask) {
    log.info("User task {} created for {}", delegateTask.getId(), delegateTask.getProcessInstanceId());
    lexClientService.notifyLexTaskCreated(LexTaskCreatedEvent.builder()
      .businessObjectIdentifier(delegateTask.getExecution().getProcessBusinessKey())
      .clientApplicationId((String) delegateTask.getVariable(LEX_CLIENT_APPLICATION_ID))
      .bridgeProcessId((String) delegateTask.getVariable(BRIDGE_PROCESS_ID))
      .clientTaskId(delegateTask.getId())
      .taskTypeId(delegateTask.getTaskDefinitionKey())
      .build());
  }

}
