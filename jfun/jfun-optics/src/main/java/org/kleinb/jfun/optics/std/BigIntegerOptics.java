package org.kleinb.jfun.optics.std;

import java.math.BigInteger;
import org.kleinb.jfun.Function1;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Prism;

public final class BigIntegerOptics {
  private BigIntegerOptics() {}

  public static Prism<BigInteger, Long> bigIntegerToLong() {
    return Prism.of(bi -> Try.of(bi::longValueExact).toOption(), BigInteger::valueOf);
  }

  public static Prism<BigInteger, Integer> bigIntegerToInteger() {
    return Prism.of(
        bi -> Try.of(bi::intValueExact).toOption(),
        (Function1<Integer, BigInteger>) BigInteger::valueOf);
  }

  public static Prism<BigInteger, Character> bigIntegerToCharacter() {
    final Function1<BigInteger, Option<Character>> getOption =
        (BigInteger bi) -> Try.of(() -> ((char) bi.longValueExact())).toOption();
    return Prism.of(getOption, (Function1<Character, BigInteger>) BigInteger::valueOf);
  }

  public static Prism<BigInteger, Byte> bigIntegerToByte() {
    return Prism.of(
        bi -> Try.of(bi::byteValueExact).toOption(),
        (Function1<Byte, BigInteger>) BigInteger::valueOf);
  }

  public static Prism<BigInteger, Boolean> bigIntegerToBoolean() {
    return Prism.of(
        bi ->
            Try.of(bi::intValueExact)
                .toOption()
                .flatMap(
                    i ->
                        switch (i) {
                          case 0 -> Option.some(false);
                          case 1 -> Option.some(true);
                          default -> Option.none();
                        }),
        b -> b ? BigInteger.ONE : BigInteger.ZERO);
  }
}
