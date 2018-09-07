package com.kn.bpa.lex.server.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_TASK_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_TASK_TYPE_ID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.communication.commands.CompleteLexTaskCommand;
import com.kn.bpa.lex.server.LexServerService;

import lombok.extern.slf4j.Slf4j;

/**
 * Notifies the remote lex that a task was completed on platform side.
 */
@Component
@Slf4j
public class LexTaskCompletedDelegate implements JavaDelegate {

  private final LexServerService lexServerService;

  @Autowired
  public LexTaskCompletedDelegate(final LexServerService lexServerService) {
    this.lexServerService = lexServerService;
  }

  @Override
  public void execute(final DelegateExecution execution) {
    log.info("Lex Task completed: {}", execution.getVariable(LEX_TASK_TYPE_ID));

    final String applicationId = (String) execution.getVariable(LEX_CLIENT_APPLICATION_ID);
    final String taskId = (String) execution.getVariable(LEX_CLIENT_TASK_ID);
    lexServerService.completeLexTask(CompleteLexTaskCommand.of(taskId, applicationId));
  }
}
