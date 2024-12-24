package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Validation<E, A> permits Invalid, Valid {
  static <E, A> Validation<E, A> valid(A value) {
    return new Valid<>(value);
  }

  static <E, A> Validation<E, A> invalid(E error) {
    return new Invalid<>(List.of(error));
  }

  static <E, A> Validation<E, A> invalid(List<E> errors) {
    return new Invalid<>(errors);
  }

  static <E, A> Validation<E, A> fromEither(Either<E, A> either) {
    return either.fold(Validation::valid, Validation::invalid);
  }

  static <A> Validation<Throwable, A> fromTry(Try<A> t) {
    return t.fold(Validation::valid, Validation::invalid);
  }

  static <E, A> Validation<E, A> fromPredicate(E error, Predicate<A> p, A a) {
    return p.test(a) ? valid(a) : invalid(error);
  }

  static <E, A1, A2, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1, Validation<E, A2> v2, Function2<A1, A2, Z> f) {
    if (v1.isValid() && v2.isValid()) {
      return valid(f.apply(v1.get(), v2.get()));
    } else {
      return invalid(errors(List.of(v1, v2)));
    }
  }

  static <E, A1, A2, A3, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Function3<A1, A2, A3, Z> f) {
    if (v1.isValid() && v2.isValid() && v3.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get()));
    } else {
      return invalid(errors(List.of(v1, v2, v3)));
    }
  }

  static <E, A1, A2, A3, A4, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Function4<A1, A2, A3, A4, Z> f) {
    if (v1.isValid() && v2.isValid() && v3.isValid() && v4.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get()));
    } else {
      return invalid(errors(List.of(v1, v2, v3, v4)));
    }
  }

  static <E, A1, A2, A3, A4, A5, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Validation<E, A5> v5,
      Function5<A1, A2, A3, A4, A5, Z> f) {
    if (v1.isValid() && v2.isValid() && v3.isValid() && v4.isValid() && v5.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get(), v5.get()));
    } else {
      return invalid(errors(List.of(v1, v2, v3, v4, v5)));
    }
  }

  static <E, A1, A2, A3, A4, A5, A6, Z> Validation<E, Z> validateWith(
      Validation<E, A1> v1,
      Validation<E, A2> v2,
      Validation<E, A3> v3,
      Validation<E, A4> v4,
      Validation<E, A5> v5,
      Validation<E, A6> v6,
      Function6<A1, A2, A3, A4, A5, A6, Z> f) {
    if (v1.isValid()
        && v2.isValid()
        && v3.isValid()
        && v4.isValid()
        && v5.isValid()
        && v6.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get(), v5.get(), v6.get()));
    } else {
      return invalid(errors(List.of(v1, v2, v3, v4, v5, v6)));
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
      Function7<A1, A2, A3, A4, A5, A6, A7, Z> f) {
    if (v1.isValid()
        && v2.isValid()
        && v3.isValid()
        && v4.isValid()
        && v5.isValid()
        && v6.isValid()
        && v7.isValid()) {
      return valid(f.apply(v1.get(), v2.get(), v3.get(), v4.get(), v5.get(), v6.get(), v7.get()));
    } else {
      return invalid(errors(List.of(v1, v2, v3, v4, v5, v6, v7)));
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
      Function8<A1, A2, A3, A4, A5, A6, A7, A8, Z> f) {
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
      return invalid(errors(List.of(v1, v2, v3, v4, v5, v6, v7, v8)));
    }
  }

  static <E> List<E> errors(List<Validation<E, ?>> validations) {
    return validations.stream()
        .filter(Validation::isInvalid)
        .flatMap(v -> v.getError().stream())
        .toList();
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

  default A getOrElse(Supplier<A> or) {
    return (this instanceof Valid(A value)) ? value : or.get();
  }

  default Validation<E, A> orElse(Validation<E, A> or) {
    return (this instanceof Valid<E, A>) ? this : or;
  }

  default Validation<E, A> orElse(Supplier<Validation<E, A>> or) {
    return (this instanceof Valid<E, A>) ? this : or.get();
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

  @SuppressWarnings("unchecked")
  default <B> Validation<E, B> map(Function<A, B> f) {
    return (this instanceof Valid(A value)) ? valid(f.apply(value)) : (Invalid<E, B>) this;
  }

  @SuppressWarnings("unchecked")
  default <B> Validation<B, A> mapError(Function<E, B> f) {
    return (this instanceof Invalid<E, A>(List<E> errs))
        ? invalid(errs.stream().map(f).toList())
        : (Valid<B, A>) this;
  }

  @SuppressWarnings("unchecked")
  default <B> Validation<E, B> flatMap(Function<A, Validation<E, B>> f) {
    return (this instanceof Valid(A value)) ? f.apply(value) : (Invalid<E, B>) this;
  }

  default <B> B fold(Function<A, B> valid, Function<List<E>, B> invalid) {
    switch (this) {
      case Valid(A value) -> {
        return valid.apply(value);
      }
      case Invalid(List<E> error) -> {
        return invalid.apply(error);
      }
    }
  }

  default Validation<E, A> tap(Consumer<A> f) {
    if (this instanceof Valid(A value)) {
      f.accept(value);
    }
    return this;
  }

  default Validation<E, A> tapError(Consumer<List<E>> f) {
    if (this instanceof Invalid(List<E> error)) {
      f.accept(error);
    }
    return this;
  }

  default Validation<E, A> tapBoth(Consumer<A> valid, Consumer<List<E>> invalid) {
    return tap(valid).tapError(invalid);
  }

  default Either<List<E>, A> toEither() {
    return fold(Either::right, Either::left);
  }

  default Option<A> toOption() {
    return fold(Option::some, _ -> Option.none());
  }

  default Try<A> toTry(Function<List<E>, Throwable> e) {
    return fold(Try::success, errs -> Try.failure(e.apply(errs)));
  }
}
