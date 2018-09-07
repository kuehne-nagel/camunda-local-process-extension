package com.kn.bpa.testapplication.lex.delegate;

import java.util.function.Function;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component
public class EvaluateCalledSubProcess implements Function<DelegateExecution, String> {

  @Override public String apply(final DelegateExecution execution) {
    // we always call the lex-process based on the activity-id
    return execution.getCurrentActivityId();
  }
}
