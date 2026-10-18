package org.kleinb.jfun;

import java.util.Objects;

/**
 * Common supertype of the immutable tuples {@link Tuple0} to {@link Tuple8}. The elements of a
 * tuple are accessed with its components {@code _1()}, {@code _2()} and so on, and may be {@code
 * null}.
 *
 * <pre>{@code
 * Tuple2<String, Integer> pair = Tuple.of("a", 1);
 * pair._1(); // "a"
 * pair.swap(); // (1, "a")
 * pair.append(true); // ("a", 1, true)
 * }</pre>
 */
public sealed interface Tuple
    permits Tuple0, Tuple1, Tuple2, Tuple3, Tuple4, Tuple5, Tuple6, Tuple7, Tuple8 {

  /**
   * Returns the empty tuple.
   *
   * @return {@link Tuple0#INSTANCE}
   */
  static Tuple0 empty() {
    return Tuple0.INSTANCE;
  }

  /**
   * Creates a pair from a map entry.
   *
   * @param entry the entry, whose key becomes the first and whose value the second element
   * @param <T1> the type of the key
   * @param <T2> the type of the value
   * @return a tuple of the key and the value of {@code entry}
   * @throws NullPointerException if {@code entry} is {@code null}
   */
  static <T1, T2> Tuple2<T1, T2> fromEntry(java.util.Map.Entry<? extends T1, ? extends T2> entry) {
    Objects.requireNonNull(entry);
    return new Tuple2<>(entry.getKey(), entry.getValue());
  }

  /**
   * Creates a tuple of one element.
   *
   * @param _1 the first element, which may be {@code null}
   * @param <T1> the type of the first element
   * @return a tuple containing the given element
   */
  static <T1> Tuple1<T1> of(T1 _1) {
    return new Tuple1<>(_1);
  }

  /**
   * Creates a tuple of two elements.
   *
   * @param _1 the first element, which may be {@code null}
   * @param _2 the second element, which may be {@code null}
   * @param <T1> the type of the first element
   * @param <T2> the type of the second element
   * @return a tuple containing the given elements
   */
  static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
    return new Tuple2<>(_1, _2);
  }

  /**
   * Creates a tuple of three elements.
   *
   * @param _1 the first element, which may be {@code null}
   * @param _2 the second element, which may be {@code null}
   * @param _3 the third element, which may be {@code null}
   * @param <T1> the type of the first element
   * @param <T2> the type of the second element
   * @param <T3> the type of the third element
   * @return a tuple containing the given elements
   */
  static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
    return new Tuple3<>(_1, _2, _3);
  }

  /**
   * Creates a tuple of four elements.
   *
   * @param _1 the first element, which may be {@code null}
   * @param _2 the second element, which may be {@code null}
   * @param _3 the third element, which may be {@code null}
   * @param _4 the fourth element, which may be {@code null}
   * @param <T1> the type of the first element
   * @param <T2> the type of the second element
   * @param <T3> the type of the third element
   * @param <T4> the type of the fourth element
   * @return a tuple containing the given elements
   */
  static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
    return new Tuple4<>(_1, _2, _3, _4);
  }

  /**
   * Creates a tuple of five elements.
   *
   * @param _1 the first element, which may be {@code null}
   * @param _2 the second element, which may be {@code null}
   * @param _3 the third element, which may be {@code null}
   * @param _4 the fourth element, which may be {@code null}
   * @param _5 the fifth element, which may be {@code null}
   * @param <T1> the type of the first element
   * @param <T2> the type of the second element
   * @param <T3> the type of the third element
   * @param <T4> the type of the fourth element
   * @param <T5> the type of the fifth element
   * @return a tuple containing the given elements
   */
  static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) {
    return new Tuple5<>(_1, _2, _3, _4, _5);
  }

  /**
   * Creates a tuple of six elements.
   *
   * @param _1 the first element, which may be {@code null}
   * @param _2 the second element, which may be {@code null}
   * @param _3 the third element, which may be {@code null}
   * @param _4 the fourth element, which may be {@code null}
   * @param _5 the fifth element, which may be {@code null}
   * @param _6 the sixth element, which may be {@code null}
   * @param <T1> the type of the first element
   * @param <T2> the type of the second element
   * @param <T3> the type of the third element
   * @param <T4> the type of the fourth element
   * @param <T5> the type of the fifth element
   * @param <T6> the type of the sixth element
   * @return a tuple containing the given elements
   */
  static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
    return new Tuple6<>(_1, _2, _3, _4, _5, _6);
  }

  /**
   * Creates a tuple of seven elements.
   *
   * @param _1 the first element, which may be {@code null}
   * @param _2 the second element, which may be {@code null}
   * @param _3 the third element, which may be {@code null}
   * @param _4 the fourth element, which may be {@code null}
   * @param _5 the fifth element, which may be {@code null}
   * @param _6 the sixth element, which may be {@code null}
   * @param _7 the seventh element, which may be {@code null}
   * @param <T1> the type of the first element
   * @param <T2> the type of the second element
   * @param <T3> the type of the third element
   * @param <T4> the type of the fourth element
   * @param <T5> the type of the fifth element
   * @param <T6> the type of the sixth element
   * @param <T7> the type of the seventh element
   * @return a tuple containing the given elements
   */
  static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7) {
    return new Tuple7<>(_1, _2, _3, _4, _5, _6, _7);
  }

  /**
   * Creates a tuple of eight elements.
   *
   * @param _1 the first element, which may be {@code null}
   * @param _2 the second element, which may be {@code null}
   * @param _3 the third element, which may be {@code null}
   * @param _4 the fourth element, which may be {@code null}
   * @param _5 the fifth element, which may be {@code null}
   * @param _6 the sixth element, which may be {@code null}
   * @param _7 the seventh element, which may be {@code null}
   * @param _8 the eighth element, which may be {@code null}
   * @param <T1> the type of the first element
   * @param <T2> the type of the second element
   * @param <T3> the type of the third element
   * @param <T4> the type of the fourth element
   * @param <T5> the type of the fifth element
   * @param <T6> the type of the sixth element
   * @param <T7> the type of the seventh element
   * @param <T8> the type of the eighth element
   * @return a tuple containing the given elements
   */
  static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(
      T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) {
    return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
  }
}
