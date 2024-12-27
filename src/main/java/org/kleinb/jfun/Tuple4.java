package org.kleinb.jfun;

import java.util.Objects;

public record Tuple4<T1, T2, T3, T4>(T1 _1, T2 _2, T3 _3, T4 _4) implements Tuple {

  public <T5> Tuple5<T1, T2, T3, T4, T5> append(T5 _5) {
    return new Tuple5<>(_1, _2, _3, _4, _5);
  }

  public <T5> Tuple5<T5, T1, T2, T3, T4> prepend(T5 _5) {
    return new Tuple5<>(_5, _1, _2, _3, _4);
  }

  public <T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple1<T5> other) {
    Objects.requireNonNull(other);
    return new Tuple5<>(_1, _2, _3, _4, other._1());
  }

  public <T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple2<T5, T6> other) {
    Objects.requireNonNull(other);
    return new Tuple6<>(_1, _2, _3, _4, other._1(), other._2());
  }

  public <T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple3<T5, T6, T7> other) {
    Objects.requireNonNull(other);
    return new Tuple7<>(_1, _2, _3, _4, other._1(), other._2(), other._3());
  }

  public <T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(
      Tuple4<T5, T6, T7, T8> other) {
    Objects.requireNonNull(other);
    return new Tuple8<>(_1, _2, _3, _4, other._1(), other._2(), other._3(), other._4());
  }
}
