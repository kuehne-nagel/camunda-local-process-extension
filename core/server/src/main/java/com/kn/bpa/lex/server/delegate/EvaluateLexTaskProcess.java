package com.kn.bpa.lex.server.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_TASK_TYPE_ID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import com.kn.bpa.lex.common.LexConstants;

/**
 * Determines the called element by reading the process variable {@link LexConstants.VARIABLE#LEX_TASK_TYPE_ID}.
 */
@Component
public class EvaluateLexTaskProcess {

  public static final String LEX_PREFIX = "LEX_";

  public String apply(final DelegateExecution execution) {
    final String taskTypeId = (String) execution.getVariable(LEX_TASK_TYPE_ID);
    return LEX_PREFIX + taskTypeId;
  }

  public static String lexTaskProcessDefinitionKey(final String taskTypeId) {
    return LEX_PREFIX + taskTypeId;
  }

}
