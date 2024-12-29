package org.kleinb.jfun;

import java.util.function.Predicate;

public interface PartialFunction<A, B> {

  static <A1 extends A, A, B> PartialFunction<A, B> caseOf(
      Class<? super A1> clazz, Function1<? super A1, ? extends B> f) {
    @SuppressWarnings("unchecked")
    Function1<A, B> f1 = (Function1<A, B>) f;
    return of(clazz::isInstance, f1);
  }

  static <A, B> PartialFunction<A, B> of(
      Predicate<? super A> isDefinedAt, Function1<? super A, ? extends B> f) {
    return new PartialFunction<>() {
      @Override
      public B apply(A a) {
        return f.apply(a);
      }

      @Override
      public boolean isDefinedAt(A a) {
        return isDefinedAt.test(a);
      }
    };
  }

  B apply(A a);

  boolean isDefinedAt(A a);

  default Function1<A, Option<B>> lift() {
    return a -> {
      if (isDefinedAt(a)) {
        return Option.some(apply(a));
      } else {
        return Option.none();
      }
    };
  }
}
