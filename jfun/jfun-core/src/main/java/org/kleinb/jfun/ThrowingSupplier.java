package org.kleinb.jfun;

/**
 * A {@link java.util.function.Supplier} whose {@link #get} may throw any {@link Throwable}, in
 * particular checked exceptions. It lets code that throws checked exceptions be passed where a
 * plain {@code Supplier} cannot be used, for example to {@link Try#of}, which captures the thrown
 * exception as a {@code Failure}.
 *
 * <pre>{@code
 * // Files.readString throws the checked IOException
 * Try<String> content = Try.of(() -> Files.readString(path));
 * }</pre>
 *
 * @param <A> the type of the supplied value
 */
@FunctionalInterface
public interface ThrowingSupplier<A> {

  /**
   * Gets a value.
   *
   * @return the supplied value
   * @throws Throwable if the computation fails
   */
  A get() throws Throwable;
}
