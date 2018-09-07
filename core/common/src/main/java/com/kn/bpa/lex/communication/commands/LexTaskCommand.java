package com.kn.bpa.lex.communication.commands;

import org.immutables.value.Value;

/**
 * Command to be sent to remote lex to do something on a lex task on a different process engine.
 */
public interface LexTaskCommand extends LexCommand {

  @Value.Parameter
  String getTaskId();
}
