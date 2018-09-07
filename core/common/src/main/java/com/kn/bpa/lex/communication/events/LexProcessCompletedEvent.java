package com.kn.bpa.lex.communication.events;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.kn.bpa.lex.common.ImmutablesStyle;
import com.kn.bpa.lex.common.LexGsonBuilder;

@Value.Immutable
@ImmutablesStyle.DeepImmutables
@Gson.TypeAdapters
public interface LexProcessCompletedEvent extends LexProcessEvent {

  static LexProcessCompletedEvent of(final String processInstanceId) {
    return ImmutableLexProcessCompletedEvent.of(processInstanceId);
  }

  static LexProcessCompletedEvent fromJson(final String json) {
    return LexGsonBuilder.INSTANCE.fromJson(json, LexProcessCompletedEvent.class);
  }
}
