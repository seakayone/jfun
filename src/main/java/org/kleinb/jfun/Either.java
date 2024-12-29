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

  default boolean exists(Predicate<? super B> p) {
    Objects.requireNonNull(p);
    return this instanceof Right(B value) && p.test(value);
  }

  default <C> C fold(
      Function<? super A, ? extends C> ifLeft, Function<? super B, ? extends C> ifRight) {
    Objects.requireNonNull(ifLeft);
    Objects.requireNonNull(ifRight);
    switch (this) {
      case Left(A value) -> {
        return ifLeft.apply(value);
      }
      case Right(B value) -> {
        return ifRight.apply(value);
      }
    }
  }

  default <C> Either<A, C> map(Function<? super B, ? extends C> f) {
    Objects.requireNonNull(f);
    if (this instanceof Right(B value)) {
      return Either.right(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Left<A, C> left = (Left<A, C>) this;
      return left;
    }
  }

  default <C> Either<C, B> mapLeft(Function<? super A, ? extends C> f) {
    Objects.requireNonNull(f);
    if (this instanceof Left(A value)) {
      return Either.left(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Right<C, B> right = (Right<C, B>) this;
      return right;
    }
  }

  default <C, D> Either<C, D> biMap(
      Function<? super A, ? extends C> f, Function<? super B, ? extends D> g) {
    Objects.requireNonNull(f);
    Objects.requireNonNull(g);
    return fold(a -> Either.left(f.apply(a)), b -> Either.right(g.apply(b)));
  }

  default <C> Either<A, C> flatMap(
      Function<? super B, ? extends Either<? extends A, ? extends C>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Right(B value)) {
      @SuppressWarnings("unchecked")
      Either<A, C> either = (Either<A, C>) f.apply(value);
      Objects.requireNonNull(either);
      return either;
    } else {
      @SuppressWarnings("unchecked")
      Left<A, C> left = (Left<A, C>) this;
      return left;
    }
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

  default A getLeft() {
    switch (this) {
      case Left(A value) -> {
        return value;
      }
      case Right(B _) -> {
        throw new NoSuchElementException("getLeft called on Right");
      }
    }
  }

  default B getOrElse(B or) {
    return (this instanceof Right(B value)) ? value : or;
  }

  default Either<A, B> orElse(Supplier<? extends Either<? extends A, ? extends B>> or) {
    Objects.requireNonNull(or);
    if (isRight()) {
      return this;
    } else {
      @SuppressWarnings("unchecked")
      Either<A, B> either = (Either<A, B>) or.get();
      Objects.requireNonNull(either);
      return either;
    }
  }

  default Option<B> filterToOption(Predicate<? super B> p) {
    Objects.requireNonNull(p);
    return (this instanceof Right(B value) && p.test(value)) ? Option.some(value) : Option.none();
  }

  default B filterOrElse(Predicate<? super B> p, B or) {
    Objects.requireNonNull(p);
    return (this instanceof Right(B value) && p.test(value)) ? value : or;
  }

  default Either<A, B> tap(Consumer<? super B> f) {
    Objects.requireNonNull(f);
    if (this instanceof Right(B value)) {
      f.accept(value);
    }
    return this;
  }

  default Either<A, B> tapLeft(Consumer<? super A> f) {
    Objects.requireNonNull(f);
    if (this instanceof Left(A value)) {
      f.accept(value);
    }
    return this;
  }

  default Either<A, B> tapBoth(Consumer<? super A> ifLeft, Consumer<? super B> ifRight) {
    Objects.requireNonNull(ifLeft);
    Objects.requireNonNull(ifRight);
    return tapLeft(ifLeft).tap(ifRight);
  }

  // conversion methods

  default Option<B> toOption() {
    return fold(_ -> Option.none(), Option::some);
  }

  default Optional<B> toOptional() {
    return fold(_ -> Optional.empty(), Optional::of);
  }

  default List<B> toList() {
    return fold(_ -> List.of(), List::of);
  }

  default Try<B> toTry(Function<? super A, ? extends Throwable> e) {
    Objects.requireNonNull(e);
    return fold(a -> Try.failure(e.apply(a)), Try::success);
  }

  default Validation<A, B> toValidation() {
    return fold(Validation::invalid, Validation::valid);
  }
}
