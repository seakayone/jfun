package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Either<A, B> permits Left, Right {

  static <A, B> Either<A, B> left(A value) {
    return new Left<>(value);
  }

  static <A, B> Either<A, B> right(B value) {
    return new Right<>(value);
  }

  default boolean isLeft() {
    return this instanceof Left;
  }

  default boolean isRight() {
    return this instanceof Right;
  }

  default Either<B, A> swap() {
    switch (this) {
      case Left(A value) -> {
        return Either.right(value);
      }
      case Right(B value) -> {
        return Either.left(value);
      }
    }
  }

  default boolean contains(B elem) {
    return exists(b -> Objects.equals(b, elem));
  }

  default boolean exists(Predicate<? super B> f) {
    return this instanceof Right(B value) && f.test(value);
  }

  default <C> C fold(
      Function<? super B, ? extends C> right, Function<? super A, ? extends C> left) {
    switch (this) {
      case Left(A value) -> {
        return left.apply(value);
      }
      case Right(B value) -> {
        return right.apply(value);
      }
    }
  }

  @SuppressWarnings("unchecked")
  default <C> Either<A, C> map(Function<? super B, ? extends C> f) {
    return (this instanceof Right(B value)) ? Either.right(f.apply(value)) : (Left<A, C>) this;
  }

  @SuppressWarnings("unchecked")
  default <C> Either<A, C> flatMap(
      Function<? super B, ? extends Either<? extends A, ? extends C>> f) {
    return (this instanceof Right(B value)) ? (Either<A, C>) f.apply(value) : (Left<A, C>) this;
  }

  default B get() {
    switch (this) {
      case Right(B value) -> {
        return value;
      }
      case Left(A _) -> {
        throw new NoSuchElementException("get called on Left");
      }
    }
  }

  default B getOrElse(B or) {
    return (this instanceof Right(B value)) ? value : or;
  }

  @SuppressWarnings("unchecked")
  default Either<A, B> orElse(Supplier<? extends Either<? extends A, ? extends B>> or) {
    return isRight() ? this : (Either<A, B>) or.get();
  }

  default Option<B> filterToOption(Predicate<? super B> p) {
    return (this instanceof Right(B value) && p.test(value)) ? Option.some(value) : Option.none();
  }

  default B filterOrElse(Predicate<? super B> p, B or) {
    return (this instanceof Right(B value) && p.test(value)) ? value : or;
  }

  default Either<A, B> tap(Consumer<? super B> f) {
    if (this instanceof Right(B value)) {
      f.accept(value);
    }
    return this;
  }

  default Either<A, B> tapLeft(Consumer<? super A> f) {
    if (this instanceof Left(A value)) {
      f.accept(value);
    }
    return this;
  }

  default Either<A, B> tapBoth(Consumer<? super A> fa, Consumer<? super B> fb) {
    return tapLeft(fa).tap(fb);
  }

  // conversion methods

  default Option<B> toOption() {
    return fold(Option::some, _ -> Option.none());
  }

  default Optional<B> toOptional() {
    return fold(Optional::of, _ -> Optional.empty());
  }

  default List<B> toList() {
    return fold(List::of, _ -> List.of());
  }

  default Try<B> toTry(Function<? super A, ? extends Throwable> e) {
    return fold(Try::success, a -> Try.failure(e.apply(a)));
  }

  default Validation<A, B> toValidation() {
    return fold(Validation::valid, Validation::invalid);
  }
}
