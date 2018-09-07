package com.kn.bpa.lex.communication.commands;

import javax.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.kn.bpa.lex.common.ImmutablesStyle;
import com.kn.bpa.lex.common.LexGsonBuilder;

/**
 * Command to be sent to remote lex to cancel a lex process on a different process engine.
 */
@Value.Immutable
@ImmutablesStyle.DeepImmutables
@Gson.TypeAdapters
public interface CancelLexProcessCommand extends LexProcessCommand {

  static CancelLexProcessCommand of(final String boi,
    final String clientApplicationId,
    final String lexBridgeProcessInstanceId,
    final String cancellationReason) {
    return builder()
      .businessObjectIdentifier(boi)
      .clientApplicationId(clientApplicationId)
      .lexBridgeProcessInstanceId(lexBridgeProcessInstanceId)
      .cancellationReason(cancellationReason)
      .build();
  }

  static ImmutableCancelLexProcessCommand.Builder builder() {
    return ImmutableCancelLexProcessCommand.builder();
  }

  static CancelLexProcessCommand fromJson(final String json) {
    return LexGsonBuilder.INSTANCE.fromJson(json, CancelLexProcessCommand.class);
  }

  @Nullable
  @Value.Parameter
  String getCancellationReason();
}
