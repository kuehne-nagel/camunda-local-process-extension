package com.kn.bpa.lex.common;

import java.util.ServiceLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

public enum LexGsonBuilder {
  INSTANCE;

  private final GsonBuilder gsonBuilder = new GsonBuilder();

  LexGsonBuilder() {
    ServiceLoader.load(TypeAdapterFactory.class).forEach(gsonBuilder::registerTypeAdapterFactory);
  }

  public Gson create() {
    return gsonBuilder
      //We need timezone info in the date - otherwise Gson is using local time to construct dates
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
      //For update scenarios we need to also include nulls in the json - so we can update to null
      .serializeNulls()
      .create();
  }

  public String toJson(final Object src) {
    return create().toJson(src);
  }

  public <T> T fromJson(final String json, final Class<T> type) {
    return create().fromJson(json, type);
  }
}
