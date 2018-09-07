package com.kn.bpa.lex.communication.commands;

import org.immutables.value.Value;

/**
 * Command to be sent to remote lex to do something on a lex process on a different process engine.
 */
public interface LexProcessCommand extends LexCommand {

  /**
   * The business identifier that represents the business case.
   *
   * @return business object idedntifier
   */
  @Value.Parameter
  String getBusinessObjectIdentifier();

  /**
   * The process instance of the host process, needed for correlation.
   *
   * @return host process instance
   */
  @Value.Parameter
  String getLexBridgeProcessInstanceId();
}
