package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
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
    switch (this) {
      case Some(A v) -> {
        return v.equals(value);
      }
      case None<A> _ -> {
        return false;
      }
    }
  }

  default boolean exists(Predicate<A> f) {
    switch (this) {
      case Some(A value) -> {
        return f.test(value);
      }
      case None<A> _ -> {
        return false;
      }
    }
  }

  default A get() {
    switch (this) {
      case Some(A value) -> {
        return value;
      }
      case None<A> _ -> {
        throw new NoSuchElementException();
      }
    }
  }

  default <B> Option<B> flatMap(Function<A, Option<B>> f) {
    switch (this) {
      case Some(A value) -> {
        return f.apply(value);
      }
      case None<A> _ -> {
        return none();
      }
    }
  }

  default <B> Option<B> map(Function<A, B> f) {
    switch (this) {
      case Some(A value) -> {
        return some(f.apply(value));
      }
      case None<A> _ -> {
        return none();
      }
    }
  }

  default <C> C fold(Function<A, C> some, Supplier<C> none) {
    switch (this) {
      case Some(A value) -> {
        return some.apply(value);
      }
      case None<A> _ -> {
        return none.get();
      }
    }
  }

  default Option<A> orElse(Supplier<Option<A>> or) {
    switch (this) {
      case Some(A _) -> {
        return this;
      }
      case None<A> _ -> {
        return or.get();
      }
    }
  }

  default A getOrNull() {
    switch (this) {
      case Some(A value) -> {
        return value;
      }
      case None<A> _ -> {
        return null;
      }
    }
  }

  default A getOrElse(A a) {
    switch (this) {
      case Some(A value) -> {
        return value;
      }
      case None<A> _ -> {
        return a;
      }
    }
  }

  default Option<A> filter(Predicate<A> f) {
    switch (this) {
      case Some(A value) -> {
        return f.test(value) ? this : none();
      }
      case None<A> _ -> {
        return none();
      }
    }
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
    switch (this) {
      case Some(A value) -> {
        return List.of(value);
      }
      case None<A> _ -> {
        return List.of();
      }
    }
  }

  default Optional<A> toOptional() {
    switch (this) {
      case Some(A value) -> {
        return Optional.of(value);
      }
      case None<A> _ -> {
        return Optional.empty();
      }
    }
  }

  default <B> Either<B, A> toRight(Supplier<B> left) {
    switch (this) {
      case Some(A value) -> {
        return Either.right(value);
      }
      case None<A> _ -> {
        return Either.left(left.get());
      }
    }
  }

  default Try<A> toTry() {
    switch (this) {
      case Some(A value) -> {
        return Try.success(value);
      }
      case None<A> _ -> {
        return Try.failure(new NoSuchElementException());
      }
    }
  }
}
