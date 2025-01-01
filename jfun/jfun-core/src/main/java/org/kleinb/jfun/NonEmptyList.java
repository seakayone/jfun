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

  static <A> NonEmptyList<A> narrow(NonEmptyList<? extends A> nel) {
    Objects.requireNonNull(nel);
    @SuppressWarnings("unchecked")
    final NonEmptyList<A> narrowed = (NonEmptyList<A>) nel;
    return narrowed;
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
    public NonEmptyList<A> tail() {
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

  static <A> NonEmptyList<A> cons(A head, NonEmptyList<? extends A> tail) {
    Objects.requireNonNull(tail);
    return new Cons<>(head, narrow(tail), 1 + tail.size());
  }

  static <A> NonEmptyList<A> of(A a0) {
    return single(a0);
  }

  static <A> NonEmptyList<A> of(A a0, A a1) {
    return cons(a0, single(a1));
  }

  static <A> NonEmptyList<A> of(A a0, A a1, A a2) {
    return cons(a0, cons(a1, single(a2)));
  }

  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3) {
    return cons(a0, cons(a1, cons(a2, single(a3))));
  }

  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4) {
    return cons(a0, cons(a1, cons(a2, cons(a3, single(a4)))));
  }

  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4, A a5) {
    return cons(a0, cons(a1, cons(a2, cons(a3, cons(a4, single(a5))))));
  }

  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4, A a5, A a6) {
    return cons(a0, cons(a1, cons(a2, cons(a3, cons(a4, cons(a5, single(a6)))))));
  }

  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4, A a5, A a6, A a7) {
    return cons(a0, cons(a1, cons(a2, cons(a3, cons(a4, cons(a5, cons(a6, single(a7))))))));
  }

  @SafeVarargs
  static <A> NonEmptyList<A> of(A a, A... as) {
    Objects.requireNonNull(as);
    var result = single(as[as.length - 1]);
    for (int i = as.length - 2; i >= 0; i--) {
      result = result.prepend(as[i]);
    }
    result = result.prepend(a);
    return result;
  }

  static <A> Option<NonEmptyList<A>> of(Iterable<? extends A> as) {
    Objects.requireNonNull(as);
    if (as instanceof NonEmptyList<? extends A> as0) {
      return Option.some(narrow(as0));
    } else if (as instanceof java.util.List<? extends A> list) {
      final ListIterator<? extends A> iterator = list.listIterator(list.size());
      if (!iterator.hasPrevious()) {
        return Option.none();
      } else {
        NonEmptyList<A> result = single(iterator.previous());
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

  default NonEmptyList<A> concat(NonEmptyList<? extends A> other) {
    Objects.requireNonNull(other);
    return foldRight(narrow(other), NonEmptyList::cons);
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
    return cons(a, this);
  }

  default NonEmptyList<A> append(A a) {
    return this.concat(single(a));
  }

  A head();

  default Option<A> headOption() {
    return Option.some(head());
  }

  Option<NonEmptyList<A>> tailOption();

  NonEmptyList<A> tail();

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

  default <B> NonEmptyList<B> flatMap(Function1<? super A, ? extends NonEmptyList<? extends B>> f) {
    Objects.requireNonNull(f);
    var result = NonEmptyList.<B>narrow(f.apply(head()));
    return switch (this.tailOption()) {
      case Option.None() -> result;
      case Some(NonEmptyList<A> tail) -> {
        for (A a : tail) {
          result = result.concat(narrow(f.apply(a)));
        }
        yield result;
      }
    };
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

  default A reduce(Function2<? super A, ? super A, ? extends A> r) {
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
