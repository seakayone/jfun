package org.kleinb.jfun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A validated value that is either {@link Valid} or {@link Invalid}. Unlike {@link Either},
 * validations are <em>accumulating</em>: combining several of them with {@code validateWith} or
 * {@link #zip} collects the errors of all invalid ones instead of stopping at the first. An {@code
 * Invalid} therefore holds a list of errors.
 *
 * <pre>{@code
 * Validation<String, Person> person =
 *     Validation.validateWith(
 *         Validation.fromPredicate("name is empty", n -> !n.isEmpty(), name),
 *         Validation.fromPredicate("age is negative", a -> a >= 0, age),
 *         Person::new);
 * // Invalid(["name is empty", "age is negative"]) if both checks fail
 * }</pre>
 *
 * @param <E> the type of the errors
 * @param <A> the type of the value
 */
public sealed interface Validation<E, A> extends Iterable<A>
    permits Validation.Invalid, Validation.Valid {
  /**
   * The success case of a {@link Validation}.
   *
   * @param value the validated value
   * @param <E> the type of the errors
   * @param <A> the type of the value
   */
  record Valid<E, A>(A value) implements Validation<E, A> {}

  /**
   * The failure case of a {@link Validation}. The errors are copied when the {@code Invalid} is
   * created, and neither the list nor any error may be {@code null}; the canonical constructor
   * throws a {@link NullPointerException} otherwise. To keep the {@code Invalid} immutable, {@link
   * #error()} returns a new mutable list on every call.
   *
   * @param error the accumulated errors
   * @param <E> the type of the errors
   * @param <A> the type of the (absent) value
   */
  record Invalid<E, A>(List<E> error) implements Validation<E, A> {
    /**
     * Creates an {@code Invalid} holding a copy of the given errors.
     *
     * @param error the accumulated errors, none of which may be {@code null}
     * @throws NullPointerException if {@code error} is {@code null} or contains {@code null}
     */
    public Invalid {
      error = List.copyOf(error);
    }

    /**
     * Returns a copy of the errors.
     *
     * @return a new mutable {@link ArrayList} of the errors; changing it does not affect this
     *     {@code Invalid}
     */
    @Override
    public List<E> error() {
      return new ArrayList<>(error);
    }
  }

  /**
   * Creates a {@code Valid} holding the given value.
   *
   * @param value the value to wrap
   * @param <E> the type of the errors
   * @param <A> the type of the value
   * @return a {@code Valid} containing {@code value}
   */
  static <E, A> Validation<E, A> valid(A value) {
    return new Valid<>(value);
  }

  /**
   * Creates a valid validation holding {@code None}.
   *
   * @param <E> the type of the errors
   * @param <A> the type of the (absent) value
   * @return a {@code Valid} holding {@link Option#none()}
   */
  static <E, A> Validation<E, Option<A>> none() {
    return valid(Option.none());
  }

  /**
   * Creates an {@code Invalid} with a single error.
   *
   * @param error the error
   * @param <E> the type of the errors
   * @param <A> the type of the (absent) value
   * @return an {@code Invalid} containing {@code error}
   * @throws NullPointerException if {@code error} is {@code null}
   */
  static <E, A> Validation<E, A> invalid(E error) {
    Objects.requireNonNull(error);
    return new Invalid<>(List.of(error));
  }

  /**
   * Creates an {@code Invalid} holding a copy of the given errors. Note that an empty collection
   * results in an {@code Invalid} without any errors.
   *
   * @param errors the errors
   * @param <E> the type of the errors
   * @param <A> the type of the (absent) value
   * @return an {@code Invalid} containing the {@code errors}
   * @throws NullPointerException if {@code errors} is {@code null}, or contains {@code null}
   */
  static <E, A> Validation<E, A> invalid(Collection<E> errors) {
    return new Invalid<>(List.copyOf(errors));
  }

  /**
   * Converts an {@link Either} to a {@code Validation}.
   *
   * @param either the either to convert
   * @param <E> the type of the errors
   * @param <A> the type of the value
   * @return a {@code Valid} holding the {@code Right} value, or an {@code Invalid} holding the
   *     {@code Left} value as its single error
   * @throws NullPointerException if {@code either} is {@code null}
   */
  static <E, A> Validation<E, A> fromEither(Either<E, A> either) {
    Objects.requireNonNull(either);
    return either.fold(Validation::invalid, Validation::valid);
  }

  /**
   * Converts a {@link Try} to a {@code Validation}.
   *
   * @param t the try to convert
   * @param <A> the type of the value
   * @return a {@code Valid} holding the value, or an {@code Invalid} holding the throwable as its
   *     single error
   * @throws NullPointerException if {@code t} is {@code null}
   */
  static <A> Validation<Throwable, A> fromTry(Try<A> t) {
    Objects.requireNonNull(t);
    return t.fold(Validation::invalid, Validation::valid);
  }

  /**
   * Validates a value against a predicate.
   *
   * @param error the error used if the value does not satisfy the predicate
   * @param p the predicate to test the value with
   * @param a the value to validate
   * @param <E> the type of the errors
   * @param <A> the type of the value
   * @return a {@code Valid} holding {@code a} if it satisfies {@code p}, otherwise an {@code
   *     Invalid} holding {@code error}
   * @throws NullPointerException if {@code p} is {@code null}
   */
  static <E, A> Validation<E, A> fromPredicate(E error, Predicate<? super A> p, A a) {
    Objects.requireNonNull(p);
    return p.test(a) ? valid(a) : invalid(error);
  }

  /**
   * Validates that a value is not {@code null}.
   *
   * @param error the error used if the value is {@code null}
   * @param a the value to validate
   * @param <E> the type of the errors
   * @param <A> the type of the value
   * @return a {@code Valid} holding {@code a} if it is not {@code null}, otherwise an {@code
   *     Invalid} holding {@code error}
   */
  static <E, A> Validation<E, A> nonNull(E error, A a) {
    return fromPredicate(error, Objects::nonNull, a);
  }

  /**
   * Widens a {@code Validation<? extends E, ? extends A>} to a {@code Validation<E, A>}. This is
   * safe because {@code Validation} is immutable.
   *
   * @param validation the validation to widen
   * @param <E> the target error type
   * @param <A> the target value type
   * @return the same instance, viewed as {@code Validation<E, A>}
   * @throws NullPointerException if {@code validation} is {@code null}
   */
  static <E, A> Validation<E, A> narrow(Validation<? extends E, ? extends A> validation) {
    Objects.requireNonNull(validation);
    @SuppressWarnings("unchecked")
    Validation<E, A> narrowed = (Validation<E, A>) validation;
    return narrowed;
  }

  /**
   * Applies a function to the value of a validation; equivalent to {@link #map}.
   *
   * @param v1 the validation
   * @param f the function to apply to the value
   * @param <E> the error type
   * @param <A1> the value type of the validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or {@code v1} if invalid
   * @throws NullPointerException if {@code v1} or {@code f} is {@code null}
   */
  static <E, A1, Z> Validation<E, Z> validateWith(Validation<E, A1> v1, Function1<A1, Z> f) {
    Objects.requireNonNull(v1);
    return v1.map(f);
  }

  /**
   * Combines 2 validations with the given function, accumulating errors: if all are valid, applies
   * {@code f} to their values; otherwise returns an {@code Invalid} holding the errors of all
   * invalid validations, in argument order.
   *
   * @param v1 the first validation
   * @param v2 the second validation
   * @param f the function combining the values
   * @param <E> the error type
   * @param <A1> the value type of the first validation
   * @param <A2> the value type of the second validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or an invalid one with all
   *     errors
   * @throws NullPointerException if any validation or {@code f} is {@code null}
   */
  static <E, A1, A2, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Function2<? super A1, ? super A2, ? extends Z> f) {
    Objects.requireNonNull(v1);
    Objects.requireNonNull(v2);
    Objects.requireNonNull(f);
    if (v1.isValid() && v2.isValid()) {
      return valid(f.apply(v1.get(), v2.get()));
    } else {
      return invalid(errors(Stream.of(v1, v2)));
    }
  }

  /**
   * Combines 3 validations with the given function, accumulating errors: if all are valid, applies
   * {@code f} to their values; otherwise returns an {@code Invalid} holding the errors of all
   * invalid validations, in argument order.
   *
   * @param v1 the first validation
   * @param v2 the second validation
   * @param v3 the third validation
   * @param f the function combining the values
   * @param <E> the error type
   * @param <A1> the value type of the first validation
   * @param <A2> the value type of the second validation
   * @param <A3> the value type of the third validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or an invalid one with all
   *     errors
   * @throws NullPointerException if any validation or {@code f} is {@code null}
   */
  static <E, A1, A2, A3, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Function3<? super A1, ? super A2, ? super A3, ? extends Z> f) {
    Objects.requireNonNull(v1);
    Objects.requireNonNull(v2);
    Objects.requireNonNull(v3);
    Objects.requireNonNull(f);
    if (v1.isValid() && v2.isValid() && v3.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get()));
    } else {
      return invalid(errors(Stream.of(v1, v2, v3)));
    }
  }

  /**
   * Combines 4 validations with the given function, accumulating errors: if all are valid, applies
   * {@code f} to their values; otherwise returns an {@code Invalid} holding the errors of all
   * invalid validations, in argument order.
   *
   * @param v1 the first validation
   * @param v2 the second validation
   * @param v3 the third validation
   * @param v4 the fourth validation
   * @param f the function combining the values
   * @param <E> the error type
   * @param <A1> the value type of the first validation
   * @param <A2> the value type of the second validation
   * @param <A3> the value type of the third validation
   * @param <A4> the value type of the fourth validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or an invalid one with all
   *     errors
   * @throws NullPointerException if any validation or {@code f} is {@code null}
   */
  static <E, A1, A2, A3, A4, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Function4<? super A1, ? super A2, ? super A3, ? super A4, ? extends Z> f) {
    Objects.requireNonNull(v1);
    Objects.requireNonNull(v2);
    Objects.requireNonNull(v3);
    Objects.requireNonNull(v4);
    Objects.requireNonNull(f);
    if (v1.isValid() && v2.isValid() && v3.isValid() && v4.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get()));
    } else {
      return invalid(errors(Stream.of(v1, v2, v3, v4)));
    }
  }

  /**
   * Combines 5 validations with the given function, accumulating errors: if all are valid, applies
   * {@code f} to their values; otherwise returns an {@code Invalid} holding the errors of all
   * invalid validations, in argument order.
   *
   * @param v1 the first validation
   * @param v2 the second validation
   * @param v3 the third validation
   * @param v4 the fourth validation
   * @param v5 the fifth validation
   * @param f the function combining the values
   * @param <E> the error type
   * @param <A1> the value type of the first validation
   * @param <A2> the value type of the second validation
   * @param <A3> the value type of the third validation
   * @param <A4> the value type of the fourth validation
   * @param <A5> the value type of the fifth validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or an invalid one with all
   *     errors
   * @throws NullPointerException if any validation or {@code f} is {@code null}
   */
  static <E, A1, A2, A3, A4, A5, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Validation<E, A5> v5,
      Function5<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? extends Z> f) {
    Objects.requireNonNull(v1);
    Objects.requireNonNull(v2);
    Objects.requireNonNull(v3);
    Objects.requireNonNull(v4);
    Objects.requireNonNull(v5);
    Objects.requireNonNull(f);
    if (v1.isValid() && v2.isValid() && v3.isValid() && v4.isValid() && v5.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get(), v5.get()));
    } else {
      return invalid(errors(Stream.of(v1, v2, v3, v4, v5)));
    }
  }

  /**
   * Combines 6 validations with the given function, accumulating errors: if all are valid, applies
   * {@code f} to their values; otherwise returns an {@code Invalid} holding the errors of all
   * invalid validations, in argument order.
   *
   * @param v1 the first validation
   * @param v2 the second validation
   * @param v3 the third validation
   * @param v4 the fourth validation
   * @param v5 the fifth validation
   * @param v6 the sixth validation
   * @param f the function combining the values
   * @param <E> the error type
   * @param <A1> the value type of the first validation
   * @param <A2> the value type of the second validation
   * @param <A3> the value type of the third validation
   * @param <A4> the value type of the fourth validation
   * @param <A5> the value type of the fifth validation
   * @param <A6> the value type of the sixth validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or an invalid one with all
   *     errors
   * @throws NullPointerException if any validation or {@code f} is {@code null}
   */
  static <E, A1, A2, A3, A4, A5, A6, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Validation<E, A5> v5,
      Validation<E, A6> v6,
      Function6<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? extends Z>
          f) {
    Objects.requireNonNull(v1);
    Objects.requireNonNull(v2);
    Objects.requireNonNull(v3);
    Objects.requireNonNull(v4);
    Objects.requireNonNull(v5);
    Objects.requireNonNull(v6);
    Objects.requireNonNull(f);
    if (v1.isValid()
        && v2.isValid()
        && v3.isValid()
        && v4.isValid()
        && v5.isValid()
        && v6.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get(), v5.get(), v6.get()));
    } else {
      return invalid(errors(Stream.of(v1, v2, v3, v4, v5, v6)));
    }
  }

  /**
   * Combines 7 validations with the given function, accumulating errors: if all are valid, applies
   * {@code f} to their values; otherwise returns an {@code Invalid} holding the errors of all
   * invalid validations, in argument order.
   *
   * @param v1 the first validation
   * @param v2 the second validation
   * @param v3 the third validation
   * @param v4 the fourth validation
   * @param v5 the fifth validation
   * @param v6 the sixth validation
   * @param v7 the seventh validation
   * @param f the function combining the values
   * @param <E> the error type
   * @param <A1> the value type of the first validation
   * @param <A2> the value type of the second validation
   * @param <A3> the value type of the third validation
   * @param <A4> the value type of the fourth validation
   * @param <A5> the value type of the fifth validation
   * @param <A6> the value type of the sixth validation
   * @param <A7> the value type of the seventh validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or an invalid one with all
   *     errors
   * @throws NullPointerException if any validation or {@code f} is {@code null}
   */
  static <E, A1, A2, A3, A4, A5, A6, A7, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Validation<E, A5> v5,
      Validation<E, A6> v6,
      Validation<E, A7> v7,
      Function7<
              ? super A1,
              ? super A2,
              ? super A3,
              ? super A4,
              ? super A5,
              ? super A6,
              ? super A7,
              ? extends Z>
          f) {
    Objects.requireNonNull(v1);
    Objects.requireNonNull(v2);
    Objects.requireNonNull(v3);
    Objects.requireNonNull(v4);
    Objects.requireNonNull(v5);
    Objects.requireNonNull(v6);
    Objects.requireNonNull(v7);
    Objects.requireNonNull(f);
    if (v1.isValid()
        && v2.isValid()
        && v3.isValid()
        && v4.isValid()
        && v5.isValid()
        && v6.isValid()
        && v7.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get(), v5.get(), v6.get(), v7.get()));
    } else {
      return invalid(errors(Stream.of(v1, v2, v3, v4, v5, v6, v7)));
    }
  }

  /**
   * Combines 8 validations with the given function, accumulating errors: if all are valid, applies
   * {@code f} to their values; otherwise returns an {@code Invalid} holding the errors of all
   * invalid validations, in argument order.
   *
   * @param v1 the first validation
   * @param v2 the second validation
   * @param v3 the third validation
   * @param v4 the fourth validation
   * @param v5 the fifth validation
   * @param v6 the sixth validation
   * @param v7 the seventh validation
   * @param v8 the eighth validation
   * @param f the function combining the values
   * @param <E> the error type
   * @param <A1> the value type of the first validation
   * @param <A2> the value type of the second validation
   * @param <A3> the value type of the third validation
   * @param <A4> the value type of the fourth validation
   * @param <A5> the value type of the fifth validation
   * @param <A6> the value type of the sixth validation
   * @param <A7> the value type of the seventh validation
   * @param <A8> the value type of the eighth validation
   * @param <Z> the result type
   * @return a valid {@code Validation} holding the result of {@code f}, or an invalid one with all
   *     errors
   * @throws NullPointerException if any validation or {@code f} is {@code null}
   */
  static <E, A1, A2, A3, A4, A5, A6, A7, A8, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Validation<E, A5> v5,
      Validation<E, A6> v6,
      Validation<E, A7> v7,
      Validation<E, A8> v8,
      Function8<
              ? super A1,
              ? super A2,
              ? super A3,
              ? super A4,
              ? super A5,
              ? super A6,
              ? super A7,
              ? super A8,
              ? extends Z>
          f) {
    Objects.requireNonNull(v1);
    Objects.requireNonNull(v2);
    Objects.requireNonNull(v3);
    Objects.requireNonNull(v4);
    Objects.requireNonNull(v5);
    Objects.requireNonNull(v6);
    Objects.requireNonNull(v7);
    Objects.requireNonNull(v8);
    Objects.requireNonNull(f);
    if (v1.isValid()
        && v2.isValid()
        && v3.isValid()
        && v4.isValid()
        && v5.isValid()
        && v6.isValid()
        && v7.isValid()
        && v8.isValid()) {
      return valid(
          f.apply(v1.get(), v2.get(), v3.get(), v4.get(), v5.get(), v6.get(), v7.get(), v8.get()));
    } else {
      return invalid(errors(Stream.of(v1, v2, v3, v4, v5, v6, v7, v8)));
    }
  }

  /**
   * Collects the errors of all invalid validations, in order.
   *
   * @param validations the validations to collect the errors from
   * @param <E> the type of the errors
   * @return a new mutable {@link ArrayList} of the errors of all invalid validations; empty if all
   *     are valid
   * @throws NullPointerException if {@code validations} is {@code null}
   */
  static <E> List<E> errors(List<Validation<E, ?>> validations) {
    Objects.requireNonNull(validations);
    return errors(validations.stream());
  }

  /**
   * Collects the errors of all invalid validations, in order.
   *
   * @param validations the validations to collect the errors from
   * @param <E> the type of the errors
   * @return a new mutable {@link ArrayList} of the errors of all invalid validations; empty if all
   *     are valid
   * @throws NullPointerException if {@code validations} is {@code null}
   */
  static <E> List<E> errors(Stream<Validation<E, ?>> validations) {
    Objects.requireNonNull(validations);
    return validations
        .filter(Validation::isInvalid)
        .flatMap(v -> v.getError().stream())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Checks whether this is a {@code Valid}.
   *
   * @return {@code true} if this is a {@code Valid}, {@code false} otherwise
   */
  default boolean isValid() {
    return this instanceof Valid;
  }

  /**
   * Checks whether this is an {@code Invalid}.
   *
   * @return {@code true} if this is an {@code Invalid}, {@code false} otherwise
   */
  default boolean isInvalid() {
    return this instanceof Invalid;
  }

  /**
   * Returns the value.
   *
   * @return the value of a {@code Valid}
   * @throws NoSuchElementException if this is an {@code Invalid}
   */
  default A get() {
    if (this instanceof Valid(A value)) {
      return value;
    } else {
      throw new NoSuchElementException("get called on Invalid");
    }
  }

  /**
   * Combines this validation with another into a validation of a {@link Tuple2}, accumulating
   * errors.
   *
   * @param other the other validation
   * @param <B> the value type of the other validation
   * @return a {@code Valid} holding both values if both are valid, otherwise an {@code Invalid}
   *     with the errors of all invalid validations
   * @throws NullPointerException if {@code other} is {@code null}
   */
  default <B> Validation<E, Tuple2<A, B>> zip(Validation<E, B> other) {
    Objects.requireNonNull(other);
    return zipWith(other, Tuple::of);
  }

  /**
   * Combines this validation with another using the given function, accumulating errors.
   *
   * @param other the other validation
   * @param f the function combining both values
   * @param <B> the value type of the other validation
   * @param <C> the type of the combined value
   * @return a {@code Valid} holding the result of {@code f} if both are valid, otherwise an {@code
   *     Invalid} with the errors of all invalid validations
   * @throws NullPointerException if {@code other} or {@code f} is {@code null}
   */
  default <B, C> Validation<E, C> zipWith(
      Validation<E, B> other, Function2<? super A, ? super B, ? extends C> f) {
    Objects.requireNonNull(other);
    Objects.requireNonNull(f);
    return Validation.validateWith(this, other, f);
  }

  /**
   * Checks whether this is a {@code Valid} whose value equals the given value.
   *
   * @param value the value to look for
   * @return {@code true} if this is a {@code Valid} and its value equals {@code value}; always
   *     {@code false} for an {@code Invalid}
   */
  default boolean contains(A value) {
    return exists(a -> Objects.equals(a, value));
  }

  /**
   * Checks whether this is a {@code Valid} whose value satisfies the given predicate.
   *
   * @param p the predicate to test the value with
   * @return {@code true} if this is a {@code Valid} and its value satisfies {@code p}; always
   *     {@code false} for an {@code Invalid}
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default boolean exists(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    return (this instanceof Valid(A value)) && p.test(value);
  }

  /**
   * Returns the errors.
   *
   * @return a new mutable {@link ArrayList} of the errors of an {@code Invalid}
   * @throws NoSuchElementException if this is a {@code Valid}
   */
  default List<E> getError() {
    if (this instanceof Invalid(List<E> error)) {
      return error;
    } else {
      throw new NoSuchElementException("getError called on Valid");
    }
  }

  /**
   * Returns the value, or a fallback if this is an {@code Invalid}.
   *
   * @param or the fallback value
   * @return the value, or {@code or} for an {@code Invalid}
   */
  default A getOrElse(A or) {
    return (this instanceof Valid(A value)) ? value : or;
  }

  /**
   * Returns the value, or a lazily evaluated fallback if this is an {@code Invalid}.
   *
   * @param or supplies the fallback value; only invoked for an {@code Invalid}
   * @return the value, or the result of {@code or} for an {@code Invalid}
   * @throws NullPointerException if {@code or} is {@code null}
   */
  default A getOrElse(Supplier<? extends A> or) {
    Objects.requireNonNull(or);
    return (this instanceof Valid(A value)) ? value : or.get();
  }

  /**
   * Returns this if it is a {@code Valid}, otherwise the alternative.
   *
   * @param or the alternative
   * @return this for a {@code Valid}, or {@code or}
   * @throws NullPointerException if {@code or} is {@code null}
   */
  default Validation<E, A> orElse(Validation<E, A> or) {
    Objects.requireNonNull(or);
    return (this instanceof Valid<E, A>) ? this : or;
  }

  /**
   * Returns this if it is a {@code Valid}, otherwise the lazily evaluated alternative.
   *
   * @param or supplies the alternative; only invoked for an {@code Invalid}
   * @return this for a {@code Valid}, or the result of {@code or}
   * @throws NullPointerException if {@code or} is {@code null}
   */
  default Validation<E, A> orElse(Supplier<? extends Validation<? extends E, ? extends A>> or) {
    Objects.requireNonNull(or);
    if (this instanceof Valid<E, A>) {
      return this;
    } else {
      @SuppressWarnings("unchecked")
      Validation<E, A> validation = (Validation<E, A>) or.get();
      Objects.requireNonNull(validation);
      return validation;
    }
  }

  /**
   * Turns a {@code Valid} into an {@code Invalid} with the value as its single error, and an {@code
   * Invalid} into a {@code Valid} holding the list of errors.
   *
   * @return the swapped validation
   * @throws NullPointerException if this is a {@code Valid} holding {@code null}, which cannot
   *     become an error
   */
  default Validation<A, List<E>> swap() {
    switch (this) {
      case Valid(A value) -> {
        return invalid(value);
      }
      case Invalid(List<E> error) -> {
        return valid(error);
      }
    }
  }

  /**
   * Applies a function to the value of a {@code Valid}.
   *
   * @param f the function to apply to the value
   * @param <B> the type of the mapped value
   * @return a {@code Valid} holding the result of {@code f}, or this {@code Invalid} unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <B> Validation<E, B> map(Function<? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    if (this instanceof Valid(A value)) {
      return valid(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Invalid<E, B> invalid = (Invalid<E, B>) this;
      return invalid;
    }
  }

  /**
   * Applies a function to each error of an {@code Invalid}.
   *
   * @param f the function to apply to each error
   * @param <B> the type of the mapped errors
   * @return an {@code Invalid} holding the mapped errors, or this {@code Valid} unchanged
   * @throws NullPointerException if {@code f} is {@code null}, or returns {@code null} for an error
   */
  default <B> Validation<B, A> mapError(Function<? super E, ? extends B> f) {
    Objects.requireNonNull(f);
    if (this instanceof Invalid<E, A>(List<E> errs)) {
      @SuppressWarnings("unchecked")
      Validation<B, A> validation = (Validation<B, A>) invalid(errs.stream().map(f).toList());
      return validation;
    } else {
      @SuppressWarnings("unchecked")
      Validation<B, A> validation = (Validation<B, A>) this;
      return validation;
    }
  }

  /**
   * Applies one of two functions depending on the case of this validation.
   *
   * @param fe the function applied to each error of an {@code Invalid}
   * @param fa the function applied to the value of a {@code Valid}
   * @param <F> the type of the mapped errors
   * @param <B> the type of the mapped value
   * @return the mapped validation
   * @throws NullPointerException if {@code fe} or {@code fa} is {@code null}
   */
  @SuppressWarnings("unchecked")
  default <F, B> Validation<F, B> bimap(
      Function<? super E, ? extends F> fe, Function<? super A, ? extends B> fa) {
    Objects.requireNonNull(fe);
    Objects.requireNonNull(fa);
    return switch (this) {
      case Valid<E, A> v -> (Validation<F, B>) v.map(fa);
      case Invalid<E, A> iv -> (Validation<F, B>) iv.mapError(fe);
    };
  }

  /**
   * Applies a validation-returning function to the value of a {@code Valid} and flattens the
   * result. This is sequential and does not accumulate errors; use {@link #zip} or {@code
   * validateWith} to accumulate the errors of independent validations.
   *
   * @param f the function to apply to the value
   * @param <B> the type of the value of the result
   * @return the result of {@code f} for a {@code Valid}, or this {@code Invalid} unchanged
   * @throws NullPointerException if {@code f} is {@code null} or returns {@code null}
   */
  default <B> Validation<E, B> flatMap(
      Function<? super A, ? extends Validation<? extends E, ? extends B>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Valid(A value)) {
      @SuppressWarnings("unchecked")
      Validation<E, B> validation = (Validation<E, B>) f.apply(value);
      Objects.requireNonNull(validation);
      return validation;
    } else {
      @SuppressWarnings("unchecked")
      Validation<E, B> validation = (Validation<E, B>) this;
      return validation;
    }
  }

  /**
   * Reduces this validation to a single value by applying the function matching its case.
   *
   * @param ifInvalid the function applied to the errors of an {@code Invalid}
   * @param ifValid the function applied to the value of a {@code Valid}
   * @param <B> the result type
   * @return the result of {@code ifInvalid} for an {@code Invalid}, or of {@code ifValid} for a
   *     {@code Valid}
   * @throws NullPointerException if {@code ifInvalid} or {@code ifValid} is {@code null}
   */
  default <B> B fold(
      Function<? super List<? super E>, ? extends B> ifInvalid,
      Function<? super A, ? extends B> ifValid) {
    Objects.requireNonNull(ifInvalid);
    Objects.requireNonNull(ifValid);
    switch (this) {
      case Valid(A value) -> {
        return ifValid.apply(value);
      }
      case Invalid(List<E> error) -> {
        return ifInvalid.apply(error);
      }
    }
  }

  /**
   * Runs a side effect on the value of a {@code Valid}.
   *
   * @param f the consumer to run if this is a {@code Valid}
   * @return this validation, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Validation<E, A> tap(Consumer<? super A> f) {
    Objects.requireNonNull(f);
    if (this instanceof Valid(A value)) {
      f.accept(value);
    }
    return this;
  }

  /**
   * Runs a side effect on the errors of an {@code Invalid}.
   *
   * @param f the consumer to run if this is an {@code Invalid}
   * @return this validation, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Validation<E, A> tapInvalid(Consumer<? super List<? super E>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Invalid(List<E> error)) {
      f.accept(error);
    }
    return this;
  }

  /**
   * Runs the side effect matching the case of this validation.
   *
   * @param ifInvalid the consumer to run if this is an {@code Invalid}
   * @param ifValid the consumer to run if this is a {@code Valid}
   * @return this validation, unchanged
   * @throws NullPointerException if {@code ifInvalid} or {@code ifValid} is {@code null}
   */
  default Validation<E, A> tapBoth(
      Consumer<? super List<? super E>> ifInvalid, Consumer<? super A> ifValid) {
    Objects.requireNonNull(ifValid);
    Objects.requireNonNull(ifInvalid);
    return tap(ifValid).tapInvalid(ifInvalid);
  }

  /**
   * Converts to an {@link Either}.
   *
   * @return a {@code Right} holding the value, or a {@code Left} holding the list of errors
   */
  default Either<List<E>, A> toEither() {
    return toEitherWith(
        errs -> {
          @SuppressWarnings("unchecked")
          List<E> cast = (List<E>) errs;
          return cast;
        });
  }

  /**
   * Converts to an {@link Either}, mapping the errors of an {@code Invalid}.
   *
   * @param f maps the list of errors to the {@code Left} value
   * @param <B> the type of the {@code Left} value
   * @return a {@code Right} holding the value, or a {@code Left} holding the result of {@code f}
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <B> Either<B, A> toEitherWith(Function<? super List<? super E>, ? extends B> f) {
    Objects.requireNonNull(f);
    return fold(errs -> Either.left(f.apply(errs)), Either::right);
  }

  /**
   * Converts to an {@link Option}.
   *
   * @return a {@code Some} holding the value, or {@code None} for an {@code Invalid}
   */
  default Option<A> toOption() {
    return fold(ignored -> Option.none(), Option::some);
  }

  /**
   * Converts to a {@link Try}.
   *
   * @param e maps the list of errors to the exception of the failure
   * @return a successful {@code Try} holding the value, or a failed one holding the exception
   *     produced by {@code e}
   * @throws NullPointerException if {@code e} is {@code null}
   */
  default Try<A> toTryWith(Function<? super List<? super E>, ? extends Throwable> e) {
    Objects.requireNonNull(e);
    return fold(errs -> Try.failure(e.apply(errs)), Try::success);
  }

  /**
   * Iterates over the value.
   *
   * @return an iterator over the single value of a {@code Valid}, or an empty iterator for an
   *     {@code Invalid}
   */
  @Override
  default Iterator<A> iterator() {
    return fold(ignored -> Iterator.empty(), Iterator::of);
  }
}
