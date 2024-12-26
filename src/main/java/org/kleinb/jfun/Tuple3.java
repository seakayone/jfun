package org.kleinb.jfun;

import java.util.Objects;

public record Tuple3<T1, T2, T3>(T1 _1, T2 _2, T3 _3) implements Tuple {

  public <T4> Tuple4<T1, T2, T3, T4> append(T4 _4) {
    return new Tuple4<>(_1, _2, _3, _4);
  }

  public <T4> Tuple4<T4, T1, T2, T3> prepend(T4 _4) {
    return new Tuple4<>(_4, _1, _2, _3);
  }

  public <T4> Tuple4<T1, T2, T3, T4> concat(Tuple1<T4> other) {
    Objects.requireNonNull(other);
    return new Tuple4<>(_1, _2, _3, other._1());
  }

  public <T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple2<T4, T5> other) {
    Objects.requireNonNull(other);
    return new Tuple5<>(_1, _2, _3, other._1(), other._2());
  }

  public <T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple3<T4, T5, T6> other) {
    Objects.requireNonNull(other);
    return new Tuple6<>(_1, _2, _3, other._1(), other._2(), other._3());
  }

  public <T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple4<T4, T5, T6, T7> other) {
    Objects.requireNonNull(other);
    return new Tuple7<>(_1, _2, _3, other._1(), other._2(), other._3(), other._4());
  }

  public <T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(
      Tuple5<T4, T5, T6, T7, T8> other) {
    Objects.requireNonNull(other);
    return new Tuple8<>(_1, _2, _3, other._1(), other._2(), other._3(), other._4(), other._5());
  }
}
