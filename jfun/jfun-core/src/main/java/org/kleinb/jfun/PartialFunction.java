package org.kleinb.jfun;

import java.util.function.Predicate;

/**
 * A function that is only defined for some of the values of its argument type, as indicated by
 * {@link #isDefinedAt}. Use {@link #lift()} to turn it into a total function returning an {@link
 * Option}.
 *
 * <pre>{@code
 * PartialFunction<Integer, Integer> divide100By =
 *     PartialFunction.of(divisor -> divisor != 0, divisor -> 100 / divisor);
 * divide100By.isDefinedAt(0); // false
 * divide100By.lift().apply(5); // Some(20)
 * divide100By.lift().apply(0); // None, instead of an ArithmeticException
 * }</pre>
 *
 * @param <A> the argument type
 * @param <B> the result type
 */
public interface PartialFunction<A, B> {

  /**
   * Creates a partial function that is defined for instances of the given class.
   *
   * <p>Calling {@link #apply} with a value that is not an instance of {@code clazz} is unspecified;
   * check {@link #isDefinedAt} or use {@link #lift()} first.
   *
   * @param clazz the class the argument must be an instance of
   * @param f the function applied to arguments that are instances of {@code clazz}
   * @param <A1> the type of the instances {@code f} accepts
   * @param <A> the argument type of the partial function
   * @param <B> the result type
   * @return a partial function defined for instances of {@code clazz}
   */
  static <A1 extends A, A, B> PartialFunction<A, B> caseOf(
      Class<? super A1> clazz, Function1<? super A1, ? extends B> f) {
    @SuppressWarnings("unchecked")
    Function1<A, B> f1 = (Function1<A, B>) f;
    return of(clazz::isInstance, f1);
  }

  /**
   * Creates a partial function from a predicate and a function.
   *
   * <p>The returned function does not check the predicate in {@link #apply}; it always delegates to
   * {@code f}.
   *
   * @param isDefinedAt tells whether the function is defined for an argument
   * @param f the function applied to arguments for which {@code isDefinedAt} holds
   * @param <A> the argument type
   * @param <B> the result type
   * @return a partial function defined where {@code isDefinedAt} holds
   */
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

  /**
   * Applies this function to the given argument. The result is only meaningful if {@link
   * #isDefinedAt} holds for it.
   *
   * @param a the argument
   * @return the result of applying this function to {@code a}
   */
  B apply(A a);

  /**
   * Checks whether this function is defined for the given argument.
   *
   * @param a the argument
   * @return {@code true} if this function is defined for {@code a}, {@code false} otherwise
   */
  boolean isDefinedAt(A a);

  /**
   * Turns this partial function into a total function that returns an {@link Option}.
   *
   * @return a function returning a {@code Some} with the result where this function is defined, and
   *     {@code None} elsewhere
   */
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
