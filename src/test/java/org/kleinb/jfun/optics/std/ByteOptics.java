package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Option;
import org.kleinb.jfun.optics.Prism;

public class ByteOptics {
  private ByteOptics() {}

  public static Prism<Byte, Boolean> byteToBoolean() {
    return Prism.of(
        b ->
            switch (b) {
              case 0 -> Option.some(false);
              case 1 -> Option.some(true);
              default -> Option.none();
            },
        b -> (byte) (b ? 1 : 0));
  }
}
