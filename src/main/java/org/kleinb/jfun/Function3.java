package org.kleinb.jfun;

@FunctionalInterface
public  interface Function3<B, C, D, A> {
    A apply(B b, C c, D d);

    default Function1<B, Function1<C, Function1<D, A>>> curried() {
        return b -> c -> d -> apply(b, c, d);
    }

    default Function1<Tuple3<B, C, D>, A> tupled() {
        return t -> apply(t._1(), t._2(), t._3());
    }

    default <E> Function3<B, C, D, E> andThen(Function1<A, E> after) {
        return (b, c, d) -> after.apply(apply(b, c, d));
    }

    default <E> Function3<E, C, D, A> compose(Function1<E, B> before) {
        return (e, c, d) -> apply(before.apply(e), c, d);
    }
}
