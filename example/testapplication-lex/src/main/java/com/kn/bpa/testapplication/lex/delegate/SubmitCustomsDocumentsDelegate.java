package com.kn.bpa.testapplication.lex.delegate;

  import org.camunda.bpm.engine.delegate.DelegateExecution;
  import org.camunda.bpm.engine.delegate.JavaDelegate;
  import org.springframework.stereotype.Component;

  import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SubmitCustomsDocumentsDelegate implements JavaDelegate {

  @Override
  public void execute(final DelegateExecution execution) {
    log.info("Submit customs documents to german customs service ...");
  }
}
