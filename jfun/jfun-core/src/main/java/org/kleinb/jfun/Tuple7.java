package org.kleinb.jfun;

import java.util.Objects;

/**
 * An immutable tuple of seven elements, accessed with {@code _1()}, {@code _2()}, {@code _3()},
 * {@code _4()}, {@code _5()}, {@code _6()}, {@code _7()}.
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
 */
public record Tuple7<T1, T2, T3, T4, T5, T6, T7>(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7)
    implements Tuple {

  /**
   * Adds an element at the end.
   *
   * @param _8 the element to add, which may be {@code null}
   * @param <T8> the type of the added element
   * @return a tuple of eight elements, with {@code _8} as its last element
   */
  public <T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> append(T8 _8) {
    return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, _8);
  }

  /**
   * Adds an element at the front.
   *
   * @param _8 the element to add, which may be {@code null}
   * @param <T8> the type of the added element
   * @return a tuple of eight elements, with {@code _8} as its first element
   */
  public <T8> Tuple8<T8, T1, T2, T3, T4, T5, T6, T7> prepend(T8 _8) {
    return new Tuple8<>(_8, _1, _2, _3, _4, _5, _6, _7);
  }

  /**
   * Appends the elements of another tuple.
   *
   * @param other the tuple whose elements are appended
   * @param <T8> the type of the first element of {@code other}
   * @return a tuple of eight elements: the elements of this tuple followed by those of {@code
   *     other}
   * @throws NullPointerException if {@code other} is {@code null}
   */
  public <T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple1<T8> other) {
    Objects.requireNonNull(other);
    return new Tuple8<>(_1, _2, _3, _4, _5, _6, _7, other._1());
  }
}
