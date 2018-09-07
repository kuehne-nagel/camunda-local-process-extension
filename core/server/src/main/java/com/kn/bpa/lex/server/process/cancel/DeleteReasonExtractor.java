package com.kn.bpa.lex.server.process.cancel;

import java.util.Optional;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

@Component
public class DeleteReasonExtractor {
  public Optional<String> getDeleteReason(final DelegateExecution execution) {
    if (!(execution instanceof ExecutionEntity)) {
      return Optional.empty();
    }

    return Optional.ofNullable(((ExecutionEntity) execution).getDeleteReason());
  }
}
