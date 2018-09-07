package com.kn.bpa.lex.server.handler;

import java.util.function.Consumer;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.common.LexConstants;
import com.kn.bpa.lex.communication.events.LexProcessCompletedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumes a {@link LexProcessCompletedEvent} and notifies the bridge process instance.
 * This will complete the bridge process.
 */
@Slf4j
@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class LexProcessCompletedHandler implements Consumer<LexProcessCompletedEvent> {

  private final RuntimeService runtimeService;

  @Autowired
  public LexProcessCompletedHandler(final RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  @Override
  public void accept(final LexProcessCompletedEvent event) {
    log.info("completing lex bridge process: {}", event);
    final String bridgeProcessId = event.getBridgeProcessId();

    if (hasActiveLexTasks(bridgeProcessId)) {
      final String message = String.format("lex bridge process instance %s cannot be completed due to active tasks", bridgeProcessId);
      log.info(message);
      throw new IllegalStateException(message);
    }
    runtimeService.createMessageCorrelation(LexConstants.MESSAGE.LEX_PROCESS_COMPLETED)
      .processInstanceId(event.getBridgeProcessId())
      .correlate();
  }

  private boolean hasActiveLexTasks(final String bridgeProcessId) {
    return runtimeService.createProcessInstanceQuery()
      .superProcessInstanceId(bridgeProcessId)
      .active()
      .count() > 0;
  }
}
