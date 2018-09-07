package com.kn.bpa.testapplication.lex.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateVariableMapping;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.stereotype.Component;

/**
 * Used for variable mapping in the call-activity in the global-process
 */
@Component
public class CalledSubVarMappingDelegate implements DelegateVariableMapping {

  @Override
  public void mapInputVariables(final DelegateExecution superExecution, final VariableMap subVariables) {
    subVariables.put(LEX_CLIENT_APPLICATION_ID, superExecution.getVariable(LEX_CLIENT_APPLICATION_ID));
  }

  @Override
  public void mapOutputVariables(final DelegateExecution superExecution, final VariableScope subInstance) {
    // nothing to do here
  }
}
