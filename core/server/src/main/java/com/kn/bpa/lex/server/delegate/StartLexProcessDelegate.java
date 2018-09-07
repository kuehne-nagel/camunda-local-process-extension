package com.kn.bpa.lex.server.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import java.util.Optional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.communication.commands.StartLexProcessCommand;
import com.kn.bpa.lex.server.LexServerService;

import lombok.extern.slf4j.Slf4j;

/**
 * Starts a remote lex process by publishing a {@link StartLexProcessCommand}.
 *
 * @see LexServerService#startLexProcess(StartLexProcessCommand)
 */
@Component
@Slf4j
public class StartLexProcessDelegate implements JavaDelegate {

  private final LexServerService lexServerService;

  @Autowired
  public StartLexProcessDelegate(final LexServerService lexServerService) {
    this.lexServerService = lexServerService;
  }

  @Override
  public void execute(final DelegateExecution execution) {
    final String businessObjectIdentifier = execution.getProcessBusinessKey();
    final String callActivityId = Optional.of(((ExecutionEntity) execution).getParentScopeExecution(true))
      .map(PvmExecutionImpl::getActivityId).get();
    log.info("Start lex process for {} and {}", callActivityId, businessObjectIdentifier);
    final StartLexProcessCommand startLexProcessCommand = StartLexProcessCommand.builder()
      .clientApplicationId((String) execution.getVariable(LEX_CLIENT_APPLICATION_ID))
      .taskTypeId(callActivityId)
      .lexBridgeProcessInstanceId(execution.getProcessInstanceId())
      .businessObjectIdentifier(businessObjectIdentifier)
      .build();

    log.info("sending: {}", startLexProcessCommand);

    this.lexServerService.startLexProcess(startLexProcessCommand);
  }
}
