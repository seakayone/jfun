package org.kleinb.jfun;

import java.util.Map;
import java.util.Objects;

public record Tuple2<T1, T2>(T1 _1, T2 _2) implements Tuple {

  public Tuple2<T2, T1> swap() {
    return new Tuple2<>(_2, _1);
  }

  public <T3> Tuple3<T1, T2, T3> append(T3 _3) {
    return new Tuple3<>(_1, _2, _3);
  }

  public <T3> Tuple3<T3, T1, T2> prepend(T3 _3) {
    return new Tuple3<>(_3, _1, _2);
  }

  public <T3> Tuple3<T1, T2, T3> concat(Tuple1<T3> other) {
    Objects.requireNonNull(other);
    return new Tuple3<>(_1, _2, other._1());
  }

  public <T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple2<T3, T4> other) {
    Objects.requireNonNull(other);
    return new Tuple4<>(_1, _2, other._1(), other._2());
  }

  public <T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple3<T3, T4, T5> other) {
    Objects.requireNonNull(other);
    return new Tuple5<>(_1, _2, other._1(), other._2(), other._3());
  }

  public <T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple4<T3, T4, T5, T6> other) {
    Objects.requireNonNull(other);
    return new Tuple6<>(_1, _2, other._1(), other._2(), other._3(), other._4());
  }

  public <T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(
      Tuple5<T3, T4, T5, T6, T7> other) {
    Objects.requireNonNull(other);
    return new Tuple7<>(_1, _2, other._1(), other._2(), other._3(), other._4(), other._5());
  }

  public <T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(
      Tuple6<T3, T4, T5, T6, T7, T8> other) {
    Objects.requireNonNull(other);
    return new Tuple8<>(
        _1, _2, other._1(), other._2(), other._3(), other._4(), other._5(), other._6());
  }

  public Map.Entry<T1, T2> toEntry() {
    return Map.entry(_1, _2);
  }
}
