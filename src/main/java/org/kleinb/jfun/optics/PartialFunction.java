package org.kleinb.jfun.optics;

import java.util.function.Predicate;
import org.kleinb.jfun.Function1;
import org.kleinb.jfun.Option;

public interface PartialFunction<A, B> {

  default PartialFunction<A, B> of(Function1<? super A, ? extends B> f) {
    return PartialFunction.of(f, _ -> true);
  }

  static <A1 extends A, A, B> PartialFunction<A, B> caseOf(
      Class<? super A1> clazz, Function1<? super A1, ? extends B> f) {
    @SuppressWarnings("unchecked")
    Function1<A, B> f1 = (Function1<A, B>) f;
    return of(f1, clazz::isInstance);
  }

  static <A, B> PartialFunction<A, B> of(
      Function1<? super A, ? extends B> f, Predicate<? super A> isDefinedAt) {
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
