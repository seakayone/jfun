package org.kleinb.jfun;

@FunctionalInterface
public  interface Function4<B, C, D, E, A> {
    A apply(B b, C c, D d, E e);

    default Function1<B, Function1<C, Function1<D, Function1<E, A>>>> curried() {
        return b -> c -> d -> e -> apply(b, c, d, e);
    }

    default Function1<Tuple4<B, C, D, E>, A> tupled() {
        return t -> apply(t._1(), t._2(), t._3(), t._4());
    }

    default <F> Function4<B, C, D, E, F> andThen(Function1<A, F> after) {
        return (b, c, d, e) -> after.apply(apply(b, c, d, e));
    }

    default <F> Function4<F, C, D, E, A> compose(Function1<F, B> before) {
        return (f, c, d, e) -> apply(before.apply(f), c, d, e);
    }
}
