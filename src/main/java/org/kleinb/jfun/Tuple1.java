package org.kleinb.jfun;

public record Tuple1<T1>(T1 _1) implements Tuple {
    public static <T1> Tuple1<T1> of(T1 _1) {
        return new Tuple1<>(_1);
    }

    public <T2> Tuple2<T1, T2> append(T2 _2) {
        return Tuple2.of(_1, _2);
    }

    public <T2> Tuple2<T2, T1> prepend(T2 _2) {
        return Tuple2.of(_2, _1);
    }

    public <T2> Tuple2<T1, T2> concat(Tuple1<T2> other) {
        return Tuple2.of(_1, other._1);
    }

    public <T2, T3> Tuple3<T1, T2, T3> concat(Tuple2<T2, T3> other) {
        return Tuple3.of(_1, other._1(), other._2());
    }

    public <T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple3<T2, T3, T4> other) {
        return Tuple4.of(_1, other._1(), other._2(), other._3());
    }

    public <T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple4<T2, T3, T4, T5> other) {
        return Tuple5.of(_1, other._1(), other._2(), other._3(), other._4());
    }

    public <T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple5<T2, T3, T4, T5, T6> other) {
        return Tuple6.of(_1, other._1(), other._2(), other._3(), other._4(), other._5());
    }

    public <T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple6<T2, T3, T4, T5, T6, T7> other) {
        return Tuple7.of(_1, other._1(), other._2(), other._3(), other._4(), other._5(), other._6());
    }

    public <T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple7<T2, T3, T4, T5, T6, T7, T8> other) {
        return Tuple8.of(_1, other._1(), other._2(), other._3(), other._4(), other._5(), other._6(), other._7());
    }
}
