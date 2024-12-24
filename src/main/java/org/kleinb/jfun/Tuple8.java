package org.kleinb.jfun;

public record Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>(
    T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) implements Tuple {
  public static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) {
    return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
  }
}
