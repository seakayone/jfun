package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Option<A> permits None, Some {

  static <A> Option<A> some(A value) {
    return new Some<>(value);
  }

  @SuppressWarnings("unchecked")
  static <A> Option<A> none() {
    return (None<A>) None.INSTANCE;
  }

  static <A> Option<A> of(A value) {
    return value == null ? none() : some(value);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  static <A> Option<A> ofOptional(Optional<A> optional) {
    return optional.map(Option::some).orElseGet(Option::none);
  }

  default boolean isSome() {
    return this instanceof Some;
  }

  default boolean isNone() {
    return this instanceof None;
  }

  default boolean contains(A value) {
    return exists(a -> Objects.equals(a, value));
  }

  default boolean exists(Predicate<A> f) {
    return (this instanceof Some(A value)) && f.test(value);
  }

  default A get() {
    if (this instanceof Some(A value)) {
      return value;
    } else {
      throw new NoSuchElementException("get called on None");
    }
  }

  default <B> Option<B> flatMap(Function<A, Option<B>> f) {
    return (this instanceof Some(A value)) ? f.apply(value) : none();
  }

  default <B> Option<B> map(Function<A, B> f) {
    return (this instanceof Some(A value)) ? some(f.apply(value)) : none();
  }

  default <C> C fold(Function<A, C> some, Supplier<C> none) {
    return (this instanceof Some(A value)) ? some.apply(value) : none.get();
  }

  default Option<A> orElse(Supplier<Option<A>> or) {
    return isSome() ? this : or.get();
  }

  default A getOrNull() {
    return (this instanceof Some(A value)) ? value : null;
  }

  default A getOrElse(A a) {
    return (this instanceof Some(A value)) ? value : a;
  }

  default Option<A> filter(Predicate<A> f) {
    return (this instanceof Some(A value) && f.test(value)) ? this : none();
  }

  default Option<A> filterNot(Predicate<A> f) {
    return filter(f.negate());
  }

  default Option<A> tap(Consumer<A> f) {
    if (this instanceof Some(A value)) {
      f.accept(value);
    }
    return this;
  }

  // Conversion methods

  default List<A> toList() {
    return fold(List::of, List::of);
  }

  default Optional<A> toOptional() {
    return fold(Optional::of, Optional::empty);
  }

  default <B> Either<B, A> toRight(Supplier<B> left) {
    return fold(Either::right, () -> Either.left(left.get()));
  }

  default Try<A> toTry() {
    return fold(Try::success, () -> Try.failure(new NoSuchElementException()));
  }
}
