package com.kn.bpa.lex.communication.commands;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.kn.bpa.lex.common.ImmutablesStyle;
import com.kn.bpa.lex.common.LexGsonBuilder;
import com.kn.bpa.lex.communication.events.LexTaskCreatedEvent;

/**
 * Command to be sent to remote lex to start a lex process on a different process engine.
 * Usertasks in the remote process will trigger a {@link LexTaskCreatedEvent} and run a single-task process on
 * the  host system.
 */
@Value.Immutable
@ImmutablesStyle.DeepImmutables
@Gson.TypeAdapters
public interface StartLexProcessCommand extends LexProcessCommand {

  static ImmutableStartLexProcessCommand.Builder builder() {
    return ImmutableStartLexProcessCommand.builder();
  }

  static StartLexProcessCommand fromJson(final String json) {
    return LexGsonBuilder.INSTANCE.fromJson(json, StartLexProcessCommand.class);
  }

  static StartLexProcessCommand of(final String boi, final String clientApplicationId, final String callActivityId, final String lexBridgeProcessInstanceId) {
    return builder()
      .businessObjectIdentifier(boi)
      .clientApplicationId(clientApplicationId)
      .taskTypeId(callActivityId)
      .lexBridgeProcessInstanceId(lexBridgeProcessInstanceId)
      .build();
  }

  /**
   * The taskTypeId of the call activity the lex process should be started for.
   *
   * @return the call activities taskTypeId
   */
  @Value.Parameter
  String getTaskTypeId();
}
