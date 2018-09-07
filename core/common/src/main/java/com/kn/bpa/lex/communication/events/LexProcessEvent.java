package com.kn.bpa.lex.communication.events;

import org.immutables.value.Value;

public interface LexProcessEvent extends LexEvent {

  @Value.Parameter
  String getBridgeProcessId();
}
