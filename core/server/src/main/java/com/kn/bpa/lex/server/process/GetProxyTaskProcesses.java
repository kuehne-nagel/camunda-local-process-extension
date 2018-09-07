package com.kn.bpa.lex.server.process;

import java.util.Collection;
import java.util.function.Function;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetProxyTaskProcesses implements Function<String, Collection<ProcessInstance>> {

  private final RuntimeService runtimeService;

  @Autowired
  public GetProxyTaskProcesses(final RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  @Override
  public Collection<ProcessInstance> apply(final String bridgeProcessId) {
    return runtimeService.createProcessInstanceQuery()
      .superProcessInstanceId(bridgeProcessId)
      .active()
      .list();
  }
}
