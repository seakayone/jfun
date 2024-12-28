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

  static <A> Option<A> none(Void ignored) {
    return none();
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
    Objects.requireNonNull(optional);
    return optional.<Option<A>>map(Option::some).orElseGet(Option::none);
  }

  static <A> Option<List<A>> sequence(Iterable<? extends Option<? extends A>> options) {
    Objects.requireNonNull(options);
    var result = new ArrayList<A>();
    for (Option<? extends A> option : options) {
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

  default boolean exists(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    return (this instanceof Some(A value)) && p.test(value);
  }

  default A get() {
    if (this instanceof Some(A value)) {
      return value;
    } else {
      throw new NoSuchElementException("No value present");
    }
  }

  default <B> Option<Tuple2<A, B>> zip(Option<B> that) {
    Objects.requireNonNull(that);
    return zipWith(that, Tuple::of);
  }

  default <B, C> Option<C> zipWith(Option<B> that, Function2<A, B, C> f) {
    Objects.requireNonNull(that);
    Objects.requireNonNull(f);
    return flatMap(a -> that.map(b -> f.apply(a, b)));
  }

  default <B> Option<B> flatMap(Function<? super A, ? extends Option<? extends B>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Some(A value)) {
      @SuppressWarnings("unchecked")
      Option<B> option = (Option<B>) f.apply(value);
      Objects.requireNonNull(option);
      return option;
    } else {
      return none();
    }
  }

  default <B> Option<B> map(Function<? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    return (this instanceof Some(A value)) ? some(f.apply(value)) : none();
  }

  default <C> C fold(Supplier<? extends C> ifNone, Function<? super A, ? extends C> ifSome) {
    Objects.requireNonNull(ifNone);
    Objects.requireNonNull(ifSome);
    return (this instanceof Some(A value)) ? ifSome.apply(value) : ifNone.get();
  }

  default Option<A> orElse(Supplier<? extends Option<? extends A>> or) {
    Objects.requireNonNull(or);
    if (this instanceof Some<A> some) {
      return some;
    } else {
      @SuppressWarnings("unchecked")
      Option<A> option = (Option<A>) or.get();
      Objects.requireNonNull(option);
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
    Objects.requireNonNull(f);
    return (this instanceof Some(A value) && f.test(value)) ? this : none();
  }

  default Option<A> filterNot(Predicate<? super A> f) {
    Objects.requireNonNull(f);
    return filter(f.negate());
  }

  default Option<A> tap(Consumer<? super A> f) {
    Objects.requireNonNull(f);
    if (this instanceof Some(A value)) {
      f.accept(value);
    }
    return this;
  }

  default Option<A> tapNone(Runnable f) {
    Objects.requireNonNull(f);
    if (this instanceof None) {
      f.run();
    }
    return this;
  }

  default Option<A> tapBoth(Runnable ifNone, Consumer<? super A> ifSome) {
    Objects.requireNonNull(ifNone);
    Objects.requireNonNull(ifSome);
    if (this instanceof Some(A value)) {
      ifSome.accept(value);
    } else {
      ifNone.run();
    }
    return this;
  }

  // Conversion methods

  default Stream<A> toStream() {
    return fold(Stream::empty, Stream::of);
  }

  default List<A> toList() {
    return fold(List::of, List::of);
  }

  default Optional<A> toOptional() {
    return fold(Optional::empty, Optional::of);
  }

  default <B> Either<B, A> toRight(Supplier<? extends B> left) {
    Objects.requireNonNull(left);
    return fold(() -> Either.left(left.get()), Either::right);
  }

  default Try<A> toTry() {
    return fold(() -> Try.failure(new NoSuchElementException()), Try::success);
  }
}
