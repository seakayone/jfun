package org.kleinb.jfun;

public record Tuple6<T1, T2, T3, T4, T5, T6>(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6)
    implements Tuple {
  public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
    return new Tuple6<>(_1, _2, _3, _4, _5, _6);
  }

  public <T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> append(T7 _7) {
    return Tuple7.of(_1, _2, _3, _4, _5, _6, _7);
  }

  public <T7> Tuple7<T7, T1, T2, T3, T4, T5, T6> prepend(T7 _7) {
    return Tuple7.of(_7, _1, _2, _3, _4, _5, _6);
  }

  public <T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple1<T7> other) {
    return Tuple7.of(_1, _2, _3, _4, _5, _6, other._1());
  }

  public <T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple2<T7, T8> other) {
    return Tuple8.of(_1, _2, _3, _4, _5, _6, other._1(), other._2());
  }
}
