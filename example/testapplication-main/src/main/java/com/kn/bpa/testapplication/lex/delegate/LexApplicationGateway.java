package com.kn.bpa.testapplication.lex.delegate;

import static com.kn.bpa.lex.common.LexConstants.VARIABLE.LEX_CLIENT_APPLICATION_ID;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LexApplicationGateway implements JavaDelegate {

  private static final String DMN_FILE = "LEX_DECISION";
  private static final String VAR_OFFICE = "office";
  private static final String VAR_LEX_APPLICATION_ID = "lexClientApplicationId";
  private static final String DMN_FALLBACK = "UNDEFINED";


  @Autowired
  private DecisionService decisionService;

  @Override
  public void execute(final DelegateExecution execution) {
    log.info("Evaluate lex application");
    String lexApplicationID = evaluateAssignmentDecisionTableWithContext(evaluateDecisionTableInput(execution.getVariable("office").toString()), DMN_FILE, VAR_LEX_APPLICATION_ID);

    log.info("Lex application is " + lexApplicationID + " for businessId " + execution.getBusinessKey() + " and activity " + execution.getActivityInstanceId());
    VariableMap variables = Variables.createVariables()
        .putValueTyped(LEX_CLIENT_APPLICATION_ID, Variables.stringValue(lexApplicationID, false));
    execution.setVariables(variables);
  }

  private Map<String, Object> evaluateDecisionTableInput(@Nonnull String officeName) {
    Map<String, Object> vars = new HashMap<>();
    vars.put(VAR_OFFICE,officeName);
    return vars;
  }

  private String evaluateAssignmentDecisionTableWithContext(final Map<String, Object> context, final String dmnKey, final String resultName) {
    final Map<String, Object> result = decisionService.evaluateDecisionTableByKey(dmnKey, context).getSingleResult();
    String resultStr =  DMN_FALLBACK;
    if (result != null && result.containsKey(resultName)) {
      resultStr = (String) result.get(resultName);
    }
    return resultStr;
  }
}
