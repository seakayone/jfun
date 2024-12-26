package org.kleinb.jfun;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public sealed interface Validation<E, A> permits Invalid, Valid {

  static <E, A> Validation<E, A> valid(A value) {
    return new Valid<>(value);
  }

  static <E, A> Validation<E, A> invalid(E error) {
    return new Invalid<>(List.of(error));
  }

  static <E, A> Validation<E, A> invalid(Collection<E> errors) {
    return new Invalid<>(List.copyOf(errors));
  }

  static <E, A> Validation<E, A> fromEither(Either<E, A> either) {
    Objects.requireNonNull(either);
    return either.fold(Validation::invalid, Validation::valid);
  }

  static <A> Validation<Throwable, A> fromTry(Try<A> t) {
    Objects.requireNonNull(t);
    return t.fold(Validation::invalid, Validation::valid);
  }

  static <E, A> Validation<E, A> fromPredicate(E error, Predicate<? super A> p, A a) {
    Objects.requireNonNull(p);
    return p.test(a) ? valid(a) : invalid(error);
  }

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

  static <E> List<E> errors(List<Validation<E, ?>> validations) {
    Objects.requireNonNull(validations);
    return errors(validations.stream());
  }

  static <E> List<E> errors(Stream<Validation<E, ?>> validations) {
    Objects.requireNonNull(validations);
    return validations.filter(Validation::isInvalid).flatMap(v -> v.getError().stream()).toList();
  }

  default boolean isValid() {
    return this instanceof Valid;
  }

  default boolean isInvalid() {
    return this instanceof Invalid;
  }

  default A get() {
    switch (this) {
      case Valid(A value) -> {
        return value;
      }
      case Invalid<E, A> _ -> {
        throw new NoSuchElementException("get called on Invalid");
      }
    }
  }

  default List<E> getError() {
    switch (this) {
      case Invalid(List<E> error) -> {
        return error;
      }
      case Valid<E, A> _ -> {
        throw new NoSuchElementException("getError called on Valid");
      }
    }
  }

  default A getOrElse(A or) {
    return (this instanceof Valid(A value)) ? value : or;
  }

  default A getOrElse(Supplier<? extends A> or) {
    Objects.requireNonNull(or);
    return (this instanceof Valid(A value)) ? value : or.get();
  }

  default Validation<E, A> orElse(Validation<E, A> or) {
    Objects.requireNonNull(or);
    return (this instanceof Valid<E, A>) ? this : or;
  }

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

  default Validation<E, A> tap(Consumer<? super A> f) {
    Objects.requireNonNull(f);
    if (this instanceof Valid(A value)) {
      f.accept(value);
    }
    return this;
  }

  default Validation<E, A> tapError(Consumer<? super List<? super E>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Invalid(List<E> error)) {
      f.accept(error);
    }
    return this;
  }

  default Validation<E, A> tapBoth(
      Consumer<? super A> valid, Consumer<? super List<? super E>> invalid) {
    Objects.requireNonNull(valid);
    Objects.requireNonNull(invalid);
    return tap(valid).tapError(invalid);
  }

  default Either<List<E>, A> toEither() {
    return fold(
        (List<? super E> errs) -> {
          @SuppressWarnings("unchecked")
          Either<List<E>, A> left = Either.left((List<E>) errs);
          return left;
        },
        Either::right);
  }

  default Option<A> toOption() {
    return fold(_ -> Option.none(), Option::some);
  }

  default Try<A> toTry(Function<? super List<? super E>, ? extends Throwable> e) {
    Objects.requireNonNull(e);
    return fold(errs -> Try.failure(e.apply(errs)), Try::success);
  }
}
