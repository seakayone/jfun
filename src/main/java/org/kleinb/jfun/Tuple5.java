package org.kleinb.jfun;

public record Tuple5<T1, T2, T3, T4, T5>(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) implements Tuple {
  public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) {
    return new Tuple5<>(_1, _2, _3, _4, _5);
  }

  public <T6> Tuple6<T1, T2, T3, T4, T5, T6> append(T6 _6) {
    return Tuple6.of(_1, _2, _3, _4, _5, _6);
  }

  public <T6> Tuple6<T6, T1, T2, T3, T4, T5> prepend(T6 _6) {
    return Tuple6.of(_6, _1, _2, _3, _4, _5);
  }

  public <T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple1<T6> other) {
    return Tuple6.of(_1, _2, _3, _4, _5, other._1());
  }

  public <T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple2<T6, T7> other) {
    return Tuple7.of(_1, _2, _3, _4, _5, other._1(), other._2());
  }

  public <T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple3<T6, T7, T8> other) {
    return Tuple8.of(_1, _2, _3, _4, _5, other._1(), other._2(), other._3());
  }
}
