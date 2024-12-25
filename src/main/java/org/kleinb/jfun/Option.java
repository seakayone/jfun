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

  static <A> Option<A> none() {
    @SuppressWarnings("unchecked")
    final var none = (None<A>) None.INSTANCE;
    return none;
  }

  static <A> Option<A> of(A value) {
    return value == null ? none() : some(value);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  static <A> Option<A> ofOptional(Optional<? extends A> optional) {
    return optional.<Option<A>>map(Option::some).orElseGet(Option::none);
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

  default boolean exists(Predicate<? super A> f) {
    return (this instanceof Some(A value)) && f.test(value);
  }

  default A get() {
    if (this instanceof Some(A value)) {
      return value;
    } else {
      throw new NoSuchElementException("No value present");
    }
  }

  @SuppressWarnings("unchecked")
  default <B> Option<B> flatMap(Function<? super A, ? extends Option<? extends B>> f) {
    return (this instanceof Some(A value)) ? (Option<B>) f.apply(value) : none();
  }

  default <B> Option<B> map(Function<? super A, ? extends B> f) {
    return (this instanceof Some(A value)) ? some(f.apply(value)) : none();
  }

  default <C> C fold(Function<? super A, ? extends C> some, Supplier<? extends C> none) {
    return (this instanceof Some(A value)) ? some.apply(value) : none.get();
  }

  @SuppressWarnings("unchecked")
  default Option<A> orElse(Supplier<? extends Option<? extends A>> or) {
    return isSome() ? this : (Option<A>) or.get();
  }

  default A getOrNull() {
    return (this instanceof Some(A value)) ? value : null;
  }

  default A getOrElse(A a) {
    return (this instanceof Some(A value)) ? value : a;
  }

  default Option<A> filter(Predicate<? super A> f) {
    return (this instanceof Some(A value) && f.test(value)) ? this : none();
  }

  default Option<A> filterNot(Predicate<? super A> f) {
    return filter(f.negate());
  }

  default Option<A> tap(Consumer<? super A> f) {
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

  default <B> Either<B, A> toRight(Supplier<? extends B> left) {
    return fold(Either::right, () -> Either.left(left.get()));
  }

  default Try<A> toTry() {
    return fold(Try::success, () -> Try.failure(new NoSuchElementException()));
  }
}
