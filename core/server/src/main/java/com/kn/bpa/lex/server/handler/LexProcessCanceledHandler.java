package com.kn.bpa.lex.server.handler;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.common.LexConstants;
import com.kn.bpa.lex.communication.events.LexProcessCanceledEvent;
import com.kn.bpa.lex.server.process.GetProxyTaskProcesses;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumes a {@link LexProcessCanceledEvent} and notifies the bridge process instance.
 * This will complete the bridge process.
 */
@Slf4j
@Component
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class LexProcessCanceledHandler implements Consumer<LexProcessCanceledEvent> {

  private final RuntimeService runtimeService;
  private final GetProxyTaskProcesses getProxyTaskProcesses;

  @Autowired
  public LexProcessCanceledHandler(final RuntimeService runtimeService, final GetProxyTaskProcesses getProxyTaskProcesses) {
    this.runtimeService = runtimeService;
    this.getProxyTaskProcesses = getProxyTaskProcesses;
  }

  @Override
  public void accept(final LexProcessCanceledEvent event) {
    log.info("canceling lex processes: {}", event);

    cancelLexTasks(event);
    finishBridgeProcess(event);
  }

  private void cancelLexTasks(final LexProcessCanceledEvent event) {
    final List<String> proxyProcessIds = getProxyTaskProcesses.apply(event.getBridgeProcessId())
      .stream()
      .map(ProcessInstance::getProcessInstanceId)
      .collect(Collectors.toList());

    final boolean skipCustomListeners = true;
    final boolean externallyTerminated = true;
    runtimeService.deleteProcessInstances(proxyProcessIds, "process was canceled from lex client", skipCustomListeners, externallyTerminated);
  }

  private void finishBridgeProcess(final LexProcessCanceledEvent event) {
    runtimeService.createMessageCorrelation(LexConstants.MESSAGE.LEX_PROCESS_CANCELED)
      .processInstanceId(event.getBridgeProcessId())
      .correlate();
  }

}
