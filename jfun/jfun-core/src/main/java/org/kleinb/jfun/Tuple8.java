package org.kleinb.jfun;

/**
 * An immutable tuple of eight elements, accessed with {@code _1()}, {@code _2()}, {@code _3()},
 * {@code _4()}, {@code _5()}, {@code _6()}, {@code _7()}, {@code _8()}.
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
 */
public record Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>(
    T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6, T7 _7, T8 _8) implements Tuple {}
