package com.kn.bpa.lex.common;

import org.immutables.value.Value;

/**
 * Allows to convert an immutable model instance to JSON-string.
 * <p>
 * For full JSON support, immutables extending this interface should also
 * <ul>
 * <li>be annotated with <code>{@literal @}Gson.TypeAdapters</code> to trigger adapter generation</li>
 * <li>provide a static <code>fromJson(String)</code> method for the reverse-conversion</li>
 * </ul>
 */
public interface ToJson {

  @Value.Auxiliary
  default String toJson() {
    return LexGsonBuilder.INSTANCE.toJson(this);
  }
}
