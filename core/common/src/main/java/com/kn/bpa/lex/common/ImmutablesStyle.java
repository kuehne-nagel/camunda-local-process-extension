package com.kn.bpa.lex.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

public final class ImmutablesStyle {

  @Target({ ElementType.PACKAGE, ElementType.TYPE })
  @Retention(RetentionPolicy.CLASS)
  @Serial.Version(1L)
  @Value.Style(
    get = { "is*", "get*" },
    create = "new",
    deepImmutablesDetection = true)
  public @interface DeepImmutables {
  }

  @Target({ ElementType.PACKAGE, ElementType.TYPE })
  @Retention(RetentionPolicy.CLASS)
  @Serial.Version(1L)
  @Value.Style(
    get = { "is*", "get*" },
    create = "new")
  public @interface Default {
  }

  @Target({ ElementType.PACKAGE, ElementType.TYPE })
  @Retention(RetentionPolicy.CLASS)
  @Serial.Version(1L)
  @Value.Style(
    get = { "is*", "get*" },
    create = "new",
    builderVisibility = Value.Style.BuilderVisibility.PACKAGE
  )
  public @interface HideBuilder {
  }

  private ImmutablesStyle() {
  }
}
