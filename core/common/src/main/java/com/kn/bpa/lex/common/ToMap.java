package com.kn.bpa.lex.common;

import java.util.Map;

import org.immutables.value.Value;

public interface ToMap {

  @Value.Auxiliary
  Map<String, Object> toMap();
}
