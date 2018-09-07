package com.kn.bpa.lex.communication.events;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.kn.bpa.lex.common.ImmutablesStyle;
import com.kn.bpa.lex.common.LexGsonBuilder;

@Value.Immutable
@ImmutablesStyle.DeepImmutables
@Gson.TypeAdapters
public interface LexTaskCreatedEvent extends LexTaskEvent {

  static ImmutableLexTaskCreatedEvent.Builder builder() {
    return ImmutableLexTaskCreatedEvent.builder();
  }

  static LexTaskCreatedEvent fromJson(final String json) {
    return LexGsonBuilder.INSTANCE.fromJson(json, LexTaskCreatedEvent.class);
  }

}
