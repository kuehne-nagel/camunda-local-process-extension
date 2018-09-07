package com.kn.bpa.lex.communication.events;

import javax.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import com.kn.bpa.lex.common.ImmutablesStyle;
import com.kn.bpa.lex.common.LexGsonBuilder;

@Value.Immutable
@ImmutablesStyle.DeepImmutables
@Gson.TypeAdapters
public interface LexTaskCanceledEvent extends LexTaskEvent {

  static ImmutableLexTaskCanceledEvent.Builder builder() {
    return ImmutableLexTaskCanceledEvent.builder();
  }

  static LexTaskCanceledEvent fromJson(final String json) {
    return LexGsonBuilder.INSTANCE.fromJson(json, LexTaskCanceledEvent.class);
  }

  @Nullable
  @Value.Parameter
  String getCancellationReason();

}

