package com.kn.bpa.lex.communication.commands;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.kn.bpa.lex.common.ImmutablesStyle;
import com.kn.bpa.lex.common.LexGsonBuilder;

@Value.Immutable
@ImmutablesStyle.DeepImmutables
@Gson.TypeAdapters
public interface CompleteLexTaskCommand extends LexTaskCommand {

  static CompleteLexTaskCommand of(final String taskId, final String clientApplicationId) {
    return ImmutableCompleteLexTaskCommand.of(taskId, clientApplicationId);
  }

  static CompleteLexTaskCommand fromJson(final String json) {
    return LexGsonBuilder.INSTANCE.fromJson(json, CompleteLexTaskCommand.class);
  }
}
