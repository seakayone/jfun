package org.kleinb.jfun;

public record Tuple4<T1, T2, T3, T4>(T1 _1, T2 _2, T3 _3, T4 _4) implements Tuple {
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
        return new Tuple4<>(_1, _2, _3, _4);
    }

    public <T5> Tuple5<T1, T2, T3, T4, T5> append(T5 _5) {
        return Tuple5.of(_1, _2, _3, _4, _5);
    }

    public <T5> Tuple5<T5, T1, T2, T3, T4> prepend(T5 _5) {
        return Tuple5.of(_5, _1, _2, _3, _4);
    }

    public <T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple1<T5> other) {
        return Tuple5.of(_1, _2, _3, _4, other._1());
    }

    public <T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple2<T5, T6> other) {
        return Tuple6.of(_1, _2, _3, _4, other._1(), other._2());
    }

    public <T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple3<T5, T6, T7> other) {
        return Tuple7.of(_1, _2, _3, _4, other._1(), other._2(), other._3());
    }

    public <T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple4<T5, T6, T7, T8> other) {
        return Tuple8.of(_1, _2, _3, _4, other._1(), other._2(), other._3(), other._4());
    }
}
