package com.kn.bpa.testapplication.lex.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LexApplicationGateway implements JavaDelegate {

  @Override
  public void execute(final DelegateExecution execution) {
    log.info("Evaluate lex application");
    execution.setVariable(LEX_CLIENT_APPLICATION_ID, "LEX_EMEA_DE");
  }
}
