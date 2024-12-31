package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Option;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Prism;

public final class IntegerOptics {
  private IntegerOptics() {}

  public static Prism<Integer, Character> integerToCharacter() {
    return Prism.of(i -> Try.of(() -> ((char) (int) i)).toOption(), Integer::valueOf);
  }

  public static Prism<Integer, Byte> integerToByte() {
    return Prism.of(i -> Try.of(i::byteValue).toOption(), Byte::intValue);
  }

  public static Prism<Integer, Boolean> integerToBoolean() {
    return Prism.of(
        i ->
            switch (i) {
              case 0 -> Option.some(false);
              case 1 -> Option.some(true);
              default -> Option.none();
            },
        b -> b ? 1 : 0);
  }
}
