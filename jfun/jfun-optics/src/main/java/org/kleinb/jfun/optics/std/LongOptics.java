package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Option;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Prism;

public final class LongOptics {
  private LongOptics() {}

  public static Prism<Long, Integer> longToInteger() {
    return Prism.of(l -> Try.of(l::intValue).toOption(), i -> (long) i);
  }

  public static Prism<Long, Character> longToCharacter() {
    return Prism.of(l -> Try.of(() -> (char) (long) l).toOption(), c -> (long) c);
  }

  public static Prism<Long, Byte> longToByte() {
    return Prism.of(l -> Try.of(l::byteValue).toOption(), b -> (long) b);
  }

  public static Prism<Long, Boolean> longToBoolean() {
    return Prism.of(
        l ->
            switch (l.intValue()) {
              case 0 -> Option.some(false);
              case 1 -> Option.some(true);
              default -> Option.none();
            },
        b -> b ? 1L : 0L);
  }
}
