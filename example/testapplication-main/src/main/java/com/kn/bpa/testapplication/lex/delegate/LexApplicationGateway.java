package com.kn.bpa.testapplication.lex.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LexApplicationGateway implements JavaDelegate {

  @Override
  public void execute(final DelegateExecution execution) {
    log.info("Evaluate lex application");
    if (execution.getVariable("office").toString().startsWith("DEKN_DEHAM"))
    {
      log.info("Configuration for a lex application found");
      VariableMap variables = Variables.createVariables()
        .putValueTyped(LEX_CLIENT_APPLICATION_ID, Variables.stringValue("LEX_EMEA_DE", true));
      execution.setVariables(variables);
    } else {
      VariableMap variables = Variables.createVariables()
        .putValueTyped(LEX_CLIENT_APPLICATION_ID, Variables.stringValue(StringUtils.EMPTY, true));
      execution.setVariables(variables);
      log.info("No configuration for a lex application found. - continue global process");
    }
  }
}
