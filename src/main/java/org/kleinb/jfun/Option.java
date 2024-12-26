package org.kleinb.jfun;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

  static <A> Option<List<A>> sequence(Iterable<Option<A>> options) {
    var result = new ArrayList<A>();
    for (Option<A> option : options) {
      if (option instanceof None) {
        return none();
      }
      result.add(option.get());
    }
    return some(Collections.unmodifiableList(result));
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

  default <B> Option<B> flatMap(Function<? super A, ? extends Option<? extends B>> f) {
    if (this instanceof Some(A value)) {
      @SuppressWarnings("unchecked")
      Option<B> option = (Option<B>) f.apply(value);
      return option;
    } else {
      return none();
    }
  }

  default <B> Option<B> map(Function<? super A, ? extends B> f) {
    return (this instanceof Some(A value)) ? some(f.apply(value)) : none();
  }

  default <C> C fold(Function<? super A, ? extends C> some, Supplier<? extends C> none) {
    return (this instanceof Some(A value)) ? some.apply(value) : none.get();
  }

  default Option<A> orElse(Supplier<? extends Option<? extends A>> or) {
    if (this instanceof Some<A> some) {
      return some;
    } else {
      @SuppressWarnings("unchecked")
      Option<A> option = (Option<A>) or.get();
      return option;
    }
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

  default Stream<A> toStream() {
    return fold(Stream::of, Stream::empty);
  }

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
