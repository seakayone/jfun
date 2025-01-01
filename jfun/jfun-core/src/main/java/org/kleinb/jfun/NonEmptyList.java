package org.kleinb.jfun;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.kleinb.jfun.Option.Some;

public sealed interface NonEmptyList<A> extends Iterable<A> {

  static <A, R> Collector<? super A, ArrayList<A>, Option<NonEmptyList<A>>> collector() {
    final BinaryOperator<ArrayList<A>> combiner =
        (as1, as2) -> {
          as1.addAll(as2);
          return as1;
        };
    return Collector.of(ArrayList::new, ArrayList::add, combiner, NonEmptyList::of);
  }

  record Single<A>(A elem, int size) implements NonEmptyList<A> {
    public Single {
      if (!Objects.equals(size, 1)) {
        throw new IllegalArgumentException("Size must be 1");
      }
    }

    @Override
    public A head() {
      return elem;
    }

    @Override
    public Option<NonEmptyList<A>> tailOption() {
      return Option.none();
    }

    @Override
    public NonEmptyList<A> tail(){
        throw new NoSuchElementException("Single does not have a tail");
    }

    @Override
    public String toString() {
      return mkString("NonEmptyList(", ", ", ")");
    }
  }

  record Cons<A>(A head, NonEmptyList<A> tail, int size) implements NonEmptyList<A> {
    public Cons {
      Objects.requireNonNull(tail);
      if (!Objects.equals(size, 1 + tail.size())) {
        throw new IllegalArgumentException("Size must be 1 + tail.size()");
      }
    }

    @Override
    public A head() {
      return head;
    }

    @Override
    public Option<NonEmptyList<A>> tailOption() {
      return Option.some(tail);
    }

    @Override
    public String toString() {
      return mkString("NonEmptyList(", ", ", ")");
    }
  }

  static <A> NonEmptyList<A> single(A a) {
    return new Single<>(a, 1);
  }

  static <A> NonEmptyList<A> cons(A head, NonEmptyList<A> tail) {
    Objects.requireNonNull(tail);
    return new Cons<>(head, tail, 1 + tail.size());
  }

  @SafeVarargs
  static <A> NonEmptyList<A> of(A a, A... as) {
    var result = single(a);
    for (A next : as) {
      result = result.prepend(next);
    }
    return result.reverse();
  }

  static <A> Option<NonEmptyList<A>> of(Iterable<? extends A> as) {
    Objects.requireNonNull(as);
    if (as instanceof NonEmptyList<? extends A>) {
      @SuppressWarnings("unchecked")
      NonEmptyList<A> as1 = (NonEmptyList<A>) as;
      return Option.some(as1);
    } else if (as instanceof java.util.List<? extends A> list) {
      final ListIterator<? extends A> iterator = list.listIterator(list.size());
      if (!iterator.hasPrevious()) {
        return Option.none();
      } else {
        var result = NonEmptyList.<A>single(iterator.previous());
        while (iterator.hasPrevious()) {
          result = result.prepend(iterator.previous());
        }
        return Option.some(result);
      }
    } else {
      return of(as.iterator());
    }
  }

  static <A> Option<NonEmptyList<A>> of(Stream<? extends A> as) {
    Objects.requireNonNull(as);
    return of(as.iterator());
  }

  static <A> Option<NonEmptyList<A>> of(java.util.Iterator<? extends A> iterator) {
    Objects.requireNonNull(iterator);
    if (!iterator.hasNext()) {
      return Option.none();
    } else {
      var result = NonEmptyList.<A>single(iterator.next());
      while (iterator.hasNext()) {
        result = result.prepend(iterator.next());
      }
      return Option.some(result.reverse());
    }
  }

  default NonEmptyList<A> concat(NonEmptyList<A> other) {
    Objects.requireNonNull(other);
    return foldRight(other, NonEmptyList::cons);
  }

  default NonEmptyList<A> reverse() {
    var it = this.iterator();
    var result = NonEmptyList.single(it.next());
    while (it.hasNext()) {
      result = result.prepend(it.next());
    }
    return result;
  }

  default NonEmptyList<A> prepend(A a) {
    return new Cons<>(a, this, 1 + this.size());
  }

  default NonEmptyList<A> append(A a) {
    return this.concat(single(a));
  }

  A head();

  default Option<A> headOption() {
    return Option.some(head());
  }

  Option<NonEmptyList<A>> tailOption();

  NonEmptyList<A> tail() ;

  int size();

  default boolean isEmpty() {
    return false;
  }

  default boolean nonEmpty() {
    return true;
  }

  default boolean contains(A elem) {
    return exists(a -> Objects.equals(a, elem));
  }

  default boolean exists(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    for (A value : this) {
      if (p.test(value)) {
        return true;
      }
    }
    return false;
  }

  default Option<A> find(Predicate<? super A> p) {
    Objects.requireNonNull(p);

    for (A value : this) {
      if (p.test(value)) {
        return Option.some(value);
      }
    }

    return Option.none();
  }

  default Option<NonEmptyList<A>> filter(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    return foldRight(
        Option.none(),
        (a, acc) ->
            switch (acc) {
              case Option.None() -> p.test(a) ? Option.some(single(a)) : Option.none();
              case Some(NonEmptyList<A> nel) ->
                  p.test(a) ? Option.some(nel.prepend(a)) : Option.some(nel);
            });
  }

  default List<A> toList() {
    final var result = new ArrayList<A>(this.size());
    this.forEach(result::add);
    return result;
  }

  default <B> NonEmptyList<B> map(Function<? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    NonEmptyList<B> result = new Single<>(f.apply(head()), 1);
    switch (this.tailOption()) {
      case Option.None() -> {
        return result;
      }
      case Some(NonEmptyList<A> tail) -> {
        for (A a : tail) {
          result = result.prepend(f.apply(a));
        }
        return result.reverse();
      }
    }
  }

  default <B> NonEmptyList<B> flatMap(Function1<? super A, NonEmptyList<B>> f) {
    Objects.requireNonNull(f);
    var fNonNull =
        f.andThen(
            e -> {
              Objects.requireNonNull(e);
              return e;
            });
    return reduceMapRight(fNonNull, (a, bs) -> fNonNull.apply(a).concat(bs));
  }

  default <B> B foldLeft(B zero, Function2<? super B, ? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    B result = zero;
    for (A a : this) {
      result = f.apply(result, a);
    }
    return result;
  }

  default <B> B foldRight(B zero, Function2<? super A, ? super B, ? extends B> f) {
    Objects.requireNonNull(f);
    return this.reverse().foldLeft(zero, f.reversed());
  }

  default A reduce(Function2<A, A, A> r) {
    Objects.requireNonNull(r);
    return reduceMapLeft(Function1.identity(), r);
  }

  default A reduceRight(Function2<? super A, ? super A, ? extends A> r) {
    Objects.requireNonNull(r);
    return reduceMapRight(Function1.identity(), r);
  }

  default <B> B reduceMapLeft(
      Function1<? super A, ? extends B> map, Function2<? super B, ? super A, ? extends B> r) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(r);
    switch (this) {
      case Single(A a, _) -> {
        return map.apply(a);
      }
      case Cons(A head, NonEmptyList<A> tail, _) -> {
        return tail.foldLeft(map.apply(head), r);
      }
    }
  }

  default <B> B reduceMapRight(
      Function1<? super A, ? extends B> map, Function2<? super A, ? super B, ? extends B> r) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(r);
    return this.reverse().reduceMapLeft(map, r.reversed());
  }

  default String mkString() {
    return mkString("");
  }

  default String mkString(String sep) {
    return mkString("", sep, "");
  }

  default String mkString(String start, String sep, String end) {
    Objects.requireNonNull(start);
    Objects.requireNonNull(sep);
    Objects.requireNonNull(end);
    return start + reduceMapLeft(a -> "" + a, (str, a) -> str + sep + a) + end;
  }

  @Override
  default Iterator<A> iterator() {
    final var self = this;
    return new Iterator<>() {
      private NonEmptyList<A> current = self;

      @Override
      public boolean hasNext() {
        return this.current != null;
      }

      @Override
      public A next() {
        return switch (current) {
          case Single(A value, _) -> {
            this.current = null;
            yield value;
          }
          case Cons(A head, NonEmptyList<A> tail, _) -> {
            this.current = tail;
            yield head;
          }
          case null -> throw new NoSuchElementException("No more elements");
        };
      }
    };
  }
}
