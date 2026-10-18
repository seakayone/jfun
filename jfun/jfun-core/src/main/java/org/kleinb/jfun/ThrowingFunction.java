package org.kleinb.jfun;

/**
 * A {@link java.util.function.Function} whose {@link #apply} may throw any {@link Throwable}, in
 * particular checked exceptions. It lets code that throws checked exceptions be passed where a
 * plain {@code Function} cannot be used, for example to {@link Try#mapTry}, which captures the
 * thrown exception as a {@code Failure}.
 *
 * <pre>{@code
 * // Files.readString throws the checked IOException
 * Try<String> content = Try.success(path).mapTry(Files::readString);
 * }</pre>
 *
 * @param <A> the argument type
 * @param <B> the result type
 */
@FunctionalInterface
public interface ThrowingFunction<A, B> {

  /**
   * Applies this function to the given argument.
   *
   * @param a the argument
   * @return the result of applying this function to {@code a}
   * @throws Throwable if the computation fails
   */
  B apply(A a) throws Throwable;
}
