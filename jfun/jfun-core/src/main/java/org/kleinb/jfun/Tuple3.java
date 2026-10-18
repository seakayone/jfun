package org.kleinb.jfun;

import java.util.Objects;

/**
 * An immutable tuple of three elements, accessed with {@code _1()}, {@code _2()}, {@code _3()}.
 *
 * @param _1 the first element, which may be {@code null}
 * @param _2 the second element, which may be {@code null}
 * @param _3 the third element, which may be {@code null}
 * @param <T1> the type of the first element
 * @param <T2> the type of the second element
 * @param <T3> the type of the third element
 */
public record Tuple3<T1, T2, T3>(T1 _1, T2 _2, T3 _3) implements Tuple {

  /**
   * Adds an element at the end.
   *
   * @param _4 the element to add, which may be {@code null}
   * @param <T4> the type of the added element
   * @return a tuple of four elements, with {@code _4} as its last element
   */
  public <T4> Tuple4<T1, T2, T3, T4> append(T4 _4) {
    return new Tuple4<>(_1, _2, _3, _4);
  }

  /**
   * Adds an element at the front.
   *
   * @param _4 the element to add, which may be {@code null}
   * @param <T4> the type of the added element
   * @return a tuple of four elements, with {@code _4} as its first element
   */
  public <T4> Tuple4<T4, T1, T2, T3> prepend(T4 _4) {
    return new Tuple4<>(_4, _1, _2, _3);
  }

  /**
   * Appends the elements of another tuple.
   *
   * @param other the tuple whose elements are appended
   * @param <T4> the type of the first element of {@code other}
   * @return a tuple of four elements: the elements of this tuple followed by those of {@code other}
   * @throws NullPointerException if {@code other} is {@code null}
   */
  public <T4> Tuple4<T1, T2, T3, T4> concat(Tuple1<T4> other) {
    Objects.requireNonNull(other);
    return new Tuple4<>(_1, _2, _3, other._1());
  }

  /**
   * Appends the elements of another tuple.
   *
   * @param other the tuple whose elements are appended
   * @param <T4> the type of the first element of {@code other}
   * @param <T5> the type of the second element of {@code other}
   * @return a tuple of five elements: the elements of this tuple followed by those of {@code other}
   * @throws NullPointerException if {@code other} is {@code null}
   */
  public <T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple2<T4, T5> other) {
    Objects.requireNonNull(other);
    return new Tuple5<>(_1, _2, _3, other._1(), other._2());
  }

  /**
   * Appends the elements of another tuple.
   *
   * @param other the tuple whose elements are appended
   * @param <T4> the type of the first element of {@code other}
   * @param <T5> the type of the second element of {@code other}
   * @param <T6> the type of the third element of {@code other}
   * @return a tuple of six elements: the elements of this tuple followed by those of {@code other}
   * @throws NullPointerException if {@code other} is {@code null}
   */
  public <T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple3<T4, T5, T6> other) {
    Objects.requireNonNull(other);
    return new Tuple6<>(_1, _2, _3, other._1(), other._2(), other._3());
  }

  /**
   * Appends the elements of another tuple.
   *
   * @param other the tuple whose elements are appended
   * @param <T4> the type of the first element of {@code other}
   * @param <T5> the type of the second element of {@code other}
   * @param <T6> the type of the third element of {@code other}
   * @param <T7> the type of the fourth element of {@code other}
   * @return a tuple of seven elements: the elements of this tuple followed by those of {@code
   *     other}
   * @throws NullPointerException if {@code other} is {@code null}
   */
  public <T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple4<T4, T5, T6, T7> other) {
    Objects.requireNonNull(other);
    return new Tuple7<>(_1, _2, _3, other._1(), other._2(), other._3(), other._4());
  }

  /**
   * Appends the elements of another tuple.
   *
   * @param other the tuple whose elements are appended
   * @param <T4> the type of the first element of {@code other}
   * @param <T5> the type of the second element of {@code other}
   * @param <T6> the type of the third element of {@code other}
   * @param <T7> the type of the fourth element of {@code other}
   * @param <T8> the type of the fifth element of {@code other}
   * @return a tuple of eight elements: the elements of this tuple followed by those of {@code
   *     other}
   * @throws NullPointerException if {@code other} is {@code null}
   */
  public <T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(
      Tuple5<T4, T5, T6, T7, T8> other) {
    Objects.requireNonNull(other);
    return new Tuple8<>(_1, _2, _3, other._1(), other._2(), other._3(), other._4(), other._5());
  }
}
