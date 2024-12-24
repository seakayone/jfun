package org.kleinb.jfun;

public record Tuple7<T1, T2, T3, T4, T5, T6, T7>(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7)
    implements Tuple {
  public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7) {
    return new Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
  }

  public <T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> append(T8 _8) {
    return Tuple8.of(_1, _2, _3, _4, _5, _6, _7, _8);
  }

  public <T8> Tuple8<T8, T1, T2, T3, T4, T5, T6, T7> prepend(T8 _8) {
    return Tuple8.of(_8, _1, _2, _3, _4, _5, _6, _7);
  }

  public <T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple1<T8> other) {
    return Tuple8.of(_1, _2, _3, _4, _5, _6, _7, other._1());
  }
}
