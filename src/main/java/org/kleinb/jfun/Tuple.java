package org.kleinb.jfun;

sealed interface Tuple
    permits Tuple0, Tuple1, Tuple2, Tuple3, Tuple4, Tuple5, Tuple6, Tuple7, Tuple8 {

  static Tuple0 empty() {
    return Tuple0.INSTANCE;
  }

  static <T1> Tuple1<T1> of(T1 _1) {
    return Tuple1.of(_1);
  }

  static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
    return Tuple2.of(_1, _2);
  }

  static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
    return Tuple3.of(_1, _2, _3);
  }

  static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
    return Tuple4.of(_1, _2, _3, _4);
  }

  static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) {
    return Tuple5.of(_1, _2, _3, _4, _5);
  }

  static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
    return Tuple6.of(_1, _2, _3, _4, _5, _6);
  }

  static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7) {
    return Tuple7.of(_1, _2, _3, _4, _5, _6, _7);
  }

  static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) {
    return Tuple8.of(_1, _2, _3, _4, _5, _6, _7, _8);
  }
}
