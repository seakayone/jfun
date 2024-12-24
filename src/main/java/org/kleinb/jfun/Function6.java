package org.kleinb.jfun;

@FunctionalInterface
public  interface Function6<B, C, D, E, F, G, A> {
    A apply(B b, C c, D d, E e, F f, G g);

    default Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, A>>>>>> curried() {
        return b -> c -> d -> e -> f -> g -> apply(b, c, d, e, f, g);
    }

    default Function1<Tuple6<B, C, D, E, F, G>, A> tupled() {
        return t -> apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6());
    }

    default <H> Function6<B, C, D, E, F, G, H> andThen(Function1<A, H> after) {
        return (b, c, d, e, f, g) -> after.apply(apply(b, c, d, e, f, g));
    }

    default <H> Function6<H, C, D, E, F, G, A> compose(Function1<H, B> before) {
        return (h, c, d, e, f, g) -> apply(before.apply(h), c, d, e, f, g);
    }
}
