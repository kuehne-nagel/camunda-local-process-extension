package com.kn.bpa.lex.server.process.cancel;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.communication.commands.CancelLexProcessCommand;
import com.kn.bpa.lex.server.LexServerService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CancellationExecutionListener implements ExecutionListener {

  public static final String LEX_BRIDGE_CANCELLED_MESSAGE = "Lex bridge process was canceled";

  private final LexServerService lexServerService;
  private final DeleteReasonExtractor deleteReasonExtractor;

  @Autowired
  public CancellationExecutionListener(final LexServerService lexServerService, final DeleteReasonExtractor deleteReasonExtractor) {
    this.lexServerService = lexServerService;
    this.deleteReasonExtractor = deleteReasonExtractor;
  }

  @Override
  public void notify(final DelegateExecution execution) {

    if (!execution.isCanceled()) {
      return;
    }

    /* the ExecutionListener is fired more than once, after the first time without variables, causing a NullPointerException,
     * therefore check for that and return if necessary */
    if (isSubProcess(execution)) {
      log.debug("returning from cancellation of {} due to missing process variables", execution);
      return;
    }

    log.info("Notify client about cancellation of process " + execution);

    final String businessObjectIdentifier = execution.getProcessBusinessKey();

    @SuppressWarnings("ConstantConditions") // client application always exists
    final CancelLexProcessCommand cancelCommand = CancelLexProcessCommand.builder()
      .businessObjectIdentifier(businessObjectIdentifier)
      .clientApplicationId((String) execution.getVariable(LEX_CLIENT_APPLICATION_ID))
      .lexBridgeProcessInstanceId(execution.getProcessInstanceId())
      .cancellationReason(deleteReasonExtractor.getDeleteReason(execution).orElse(LEX_BRIDGE_CANCELLED_MESSAGE))
      .build();

    lexServerService.cancelLexProcess(cancelCommand);
  }

  private boolean isSubProcess(final DelegateExecution execution) {
    return execution.getVariables().isEmpty();
  }
}
