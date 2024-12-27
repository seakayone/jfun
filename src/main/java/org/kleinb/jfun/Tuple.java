package org.kleinb.jfun;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public sealed interface Tuple
    permits Tuple0, Tuple1, Tuple2, Tuple3, Tuple4, Tuple5, Tuple6, Tuple7, Tuple8 {

  static <A> Tuple0<A> empty() {
    @SuppressWarnings("unchecked")
    Tuple0<A> instance = (Tuple0<A>) Tuple0.INSTANCE;
    return instance;
  }

  static <T1, T2> Tuple2<T1, T2> fromEntry(java.util.Map.Entry<? extends T1, ? extends T2> entry) {
    Objects.requireNonNull(entry);
    return new Tuple2<>(entry.getKey(), entry.getValue());
  }

  static <T1> Tuple1<T1> of(T1 _1) {
    return new Tuple1<>(_1);
  }

  static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
    return new Tuple2<>(_1, _2);
  }

  static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
    return new Tuple3<>(_1, _2, _3);
  }

  static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
    return new Tuple4<>(_1, _2, _3, _4);
  }

  static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) {
    return new Tuple5<>(_1, _2, _3, _4, _5);
  }

  static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
    return new Tuple6<>(_1, _2, _3, _4, _5, _6);
  }

  static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7) {
    return new Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
  }

  static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) {
    return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
  }

  static <K, V> Map<K, V> toMap(Iterable<Tuple2<? extends K, ? extends V>> tuples) {
    return StreamSupport.stream(tuples.spliterator(), false)
        .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2));
  }
}
