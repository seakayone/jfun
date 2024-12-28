package org.kleinb.jfun.optics;

public interface LensLaws {
    static <S, A> boolean getReplace(Lens<S, A> l, S s) {
        return l.replace(s, l.get(s)).equals(s);
    }

    static <S, A> boolean replaceGet(Lens<S, A> l, S s, A a) {
        return l.get(l.replace(s, a)).equals(a);
    }
}
