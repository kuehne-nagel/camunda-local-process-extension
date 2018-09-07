package com.kn.bpa.lex.client.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.BRIDGE_PROCESS_ID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.client.LexClientService;
import com.kn.bpa.lex.communication.events.LexProcessCompletedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Notify platform that a lex-process ended.
 */
@Component
@Slf4j
public class LexProcessEndedDelegate implements JavaDelegate {

  private final LexClientService lexClientService;

  @Autowired
  public LexProcessEndedDelegate(final LexClientService lexClientService) {
    this.lexClientService = lexClientService;
  }

  @Override
  public void execute(final DelegateExecution execution) {
    final String processId = (String) execution.getVariable(BRIDGE_PROCESS_ID);
    log.info("Lex process {} was completed", processId);

    lexClientService.notifyLexProcessCompleted(LexProcessCompletedEvent.of(processId));
  }
}
