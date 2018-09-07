package com.kn.bpa.lex.communication.commands;

import org.immutables.value.Value;

import com.kn.bpa.lex.common.ToJson;

/**
 * Command to be sent to remote lex to do something on a lex activity on a different process engine.
 */
public interface LexCommand extends ToJson {

  /**
   * The applicationId of the lex.
   *
   * @return the lex client applicationId
   */
  @Value.Parameter
  String getClientApplicationId();
}
