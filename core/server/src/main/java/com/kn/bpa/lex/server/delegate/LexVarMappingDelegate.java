package com.kn.bpa.lex.server.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_TASK_ID;
import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_TASK_TYPE_ID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateVariableMapping;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.stereotype.Component;

/**
 * Used for variable mapping in the call-activity for the lex-task-process
 */
@Component
public class LexVarMappingDelegate implements DelegateVariableMapping {

  @Override
  public void mapInputVariables(final DelegateExecution superExecution, final VariableMap subVariables) {
    subVariables.put(LEX_CLIENT_APPLICATION_ID, superExecution.getVariable(LEX_CLIENT_APPLICATION_ID));
    subVariables.put(LEX_CLIENT_TASK_ID, superExecution.getVariable(LEX_CLIENT_TASK_ID));
    subVariables.put(LEX_TASK_TYPE_ID, superExecution.getVariable(LEX_TASK_TYPE_ID));
  }

  @Override
  public void mapOutputVariables(final DelegateExecution superExecution, final VariableScope subInstance) {
    // nothing to do here
  }
}
