package com.kn.bpa.lex.communication.events;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.kn.bpa.lex.common.ImmutablesStyle;
import com.kn.bpa.lex.common.LexGsonBuilder;

@Value.Immutable
@ImmutablesStyle.DeepImmutables
@Gson.TypeAdapters
public interface LexProcessCanceledEvent extends LexProcessEvent {

  static LexProcessCanceledEvent of(final String processInstanceId) {
    return ImmutableLexProcessCanceledEvent.of(processInstanceId);
  }

  static LexProcessCanceledEvent fromJson(final String json) {
    return LexGsonBuilder.INSTANCE.fromJson(json, LexProcessCanceledEvent.class);
  }
}
