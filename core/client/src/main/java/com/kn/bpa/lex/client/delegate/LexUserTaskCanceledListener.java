package com.kn.bpa.lex.client.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.BRIDGE_PROCESS_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.client.LexClientService;
import com.kn.bpa.lex.communication.events.LexTaskCanceledEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LexUserTaskCanceledListener implements TaskListener {

  @Autowired
  private LexClientService lexClientService;

  @Override
  public void notify(final DelegateTask delegateTask) {
    lexClientService.notifyLexTaskCanceled(LexTaskCanceledEvent.builder()
      .clientApplicationId((String) delegateTask.getVariable(LEX_CLIENT_APPLICATION_ID))
      .clientTaskId(delegateTask.getId())
      .businessObjectIdentifier(delegateTask.getExecution().getProcessBusinessKey())
      .taskTypeId(delegateTask.getTaskDefinitionKey())
      .bridgeProcessId((String) delegateTask.getVariable(BRIDGE_PROCESS_ID))
      .cancellationReason(delegateTask.getDeleteReason())
      .build());
  }
}
