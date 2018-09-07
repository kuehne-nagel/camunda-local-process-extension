package com.kn.bpa.lex.communication.events;

import javax.annotation.Nullable;

import org.immutables.value.Value;

public interface LexTaskEvent extends LexEvent {

  @Value.Parameter
  String getBusinessObjectIdentifier();

  @Nullable
  @Value.Parameter
  String getBridgeProcessId();

  @Value.Parameter
  String getTaskTypeId();

  @Value.Parameter
  String getClientTaskId();

  @Value.Parameter
  String getClientApplicationId();

  @Value.Derived
  default boolean hasBridgeProcessId() {
    return getBridgeProcessId() != null;
  }
}
