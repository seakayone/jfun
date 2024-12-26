package org.kleinb.jfun;

public record Tuple0() implements Tuple {

  public static Tuple0 INSTANCE = new Tuple0();

  public static <T1> Tuple1<T1> append(T1 _1) {
    return new Tuple1<>(_1);
  }

  public static <T1> Tuple1<T1> prepend(T1 _1) {
    return new Tuple1<>(_1);
  }

  public static <T1> Tuple1<T1> concat(Tuple1<T1> other) {
    return new Tuple1<>(other._1());
  }

  public static <T1, T2> Tuple2<T1, T2> concat(Tuple2<T1, T2> other) {
    return new Tuple2<>(other._1(), other._2());
  }

  public static <T1, T2, T3> Tuple3<T1, T2, T3> concat(Tuple3<T1, T2, T3> other) {
    return new Tuple3<>(other._1(), other._2(), other._3());
  }

  public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple4<T1, T2, T3, T4> other) {
    return new Tuple4<>(other._1(), other._2(), other._3(), other._4());
  }

  public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(
      Tuple5<T1, T2, T3, T4, T5> other) {
    return new Tuple5<>(other._1(), other._2(), other._3(), other._4(), other._5());
  }

  public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(
      Tuple6<T1, T2, T3, T4, T5, T6> other) {
    return new Tuple6<>(other._1(), other._2(), other._3(), other._4(), other._5(), other._6());
  }

  public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(
      Tuple7<T1, T2, T3, T4, T5, T6, T7> other) {
    return new Tuple7<>(
        other._1(), other._2(), other._3(), other._4(), other._5(), other._6(), other._7());
  }

  public static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(
      Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> other) {
    return new Tuple8<>(
        other._1(),
        other._2(),
        other._3(),
        other._4(),
        other._5(),
        other._6(),
        other._7(),
        other._8());
  }
}
