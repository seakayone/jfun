package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Option;
import org.kleinb.jfun.optics.Prism;

public final class CharacterOptics {
  private CharacterOptics() {}

  public static Prism<Character, Boolean> charToBoolean() {
    return Prism.of(
        c ->
            switch (c) {
              case '0' -> Option.some(false);
              case '1' -> Option.some(true);
              default -> Option.none();
            },
        b -> b ? '1' : '0');
  }
}
