package org.kleinb.jfun;

@FunctionalInterface
public interface Function8<B, C, D, E, F, G, H, J, A> {
    A apply(B b, C c, D d, E e, F f, G g, H h, J j);

    default Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, Function1<H, Function1<J, A>>>>>>>> curried() {
        return b -> c -> d -> e -> f -> g -> h -> j -> apply(b, c, d, e, f, g, h, j);
    }

    default Function1<Tuple8<B, C, D, E, F, G, H, J>, A> tupled() {
        return t -> apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8());
    }

    default <K> Function8<B, C, D, E, F, G, H, J, K> andThen(Function1<A, K> after) {
        return (b, c, d, e, f, g, h, j) -> after.apply(apply(b, c, d, e, f, g, h, j));
    }

    default <K> Function8<K, C, D, E, F, G, H, J, A> compose(Function1<K, B> before) {
        return (k, c, d, e, f, g, h, j) -> apply(before.apply(k), c, d, e, f, g, h, j);
    }
}
