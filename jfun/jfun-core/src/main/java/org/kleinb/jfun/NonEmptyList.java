package org.kleinb.jfun;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.kleinb.jfun.Option.Some;

/**
 * An immutable list that always contains at least one element, so that {@link #head()} is total. It
 * is a sealed interface with two cases: {@link Single} (one element) and {@link Cons} (a head
 * followed by a non-empty tail). Operations that may leave no elements, such as {@link #filter},
 * return an {@link Option}.
 *
 * <pre>{@code
 * NonEmptyList<Integer> nel = NonEmptyList.of(1, 2, 3);
 * nel.head(); // 1
 * nel.map(i -> i * 2); // NonEmptyList(2, 4, 6)
 * NonEmptyList.of(List.of()); // None
 * }</pre>
 *
 * @param <A> the element type
 */
public sealed interface NonEmptyList<A> extends Iterable<A> {

  /**
   * Returns a collector that gathers the elements of a stream into a list. The result is an {@link
   * Option} because a stream may be empty.
   *
   * @param <A> the element type
   * @return a collector yielding a {@code Some} holding the elements in encounter order, or {@code
   *     None} if the stream was empty
   */
  static <A> Collector<? super A, ArrayList<A>, Option<NonEmptyList<A>>> collector() {
    final BinaryOperator<ArrayList<A>> combiner =
        (as1, as2) -> {
          as1.addAll(as2);
          return as1;
        };
    return Collector.of(ArrayList::new, ArrayList::add, combiner, NonEmptyList::of);
  }

  /**
   * Widens a {@code NonEmptyList<? extends A>} to a {@code NonEmptyList<A>}. This is safe because
   * {@code NonEmptyList} is immutable.
   *
   * @param nel the list to widen
   * @param <A> the target element type
   * @return the same instance, viewed as {@code NonEmptyList<A>}
   * @throws NullPointerException if {@code nel} is {@code null}
   */
  static <A> NonEmptyList<A> narrow(NonEmptyList<? extends A> nel) {
    Objects.requireNonNull(nel);
    @SuppressWarnings("unchecked")
    final NonEmptyList<A> narrowed = (NonEmptyList<A>) nel;
    return narrowed;
  }

  /**
   * A list with exactly one element.
   *
   * <p>The canonical constructor requires {@code size == 1}; prefer {@link NonEmptyList#single}.
   *
   * @param elem the only element, which may be {@code null}
   * @param size the number of elements, which must be 1
   * @param <A> the element type
   */
  record Single<A>(A elem, int size) implements NonEmptyList<A> {
    /**
     * Creates a single-element list.
     *
     * @throws IllegalArgumentException if {@code size} is not 1
     */
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

  /**
   * A list consisting of a head element followed by a non-empty tail.
   *
   * <p>The canonical constructor requires {@code size == 1 + tail.size()}; prefer {@link
   * NonEmptyList#cons}.
   *
   * @param head the first element, which may be {@code null}
   * @param tail the remaining elements
   * @param size the number of elements, which must be {@code 1 + tail.size()}
   * @param <A> the element type
   */
  record Cons<A>(A head, NonEmptyList<A> tail, int size) implements NonEmptyList<A> {
    /**
     * Creates a list from a head and a tail.
     *
     * @throws NullPointerException if {@code tail} is {@code null}
     * @throws IllegalArgumentException if {@code size} is not {@code 1 + tail.size()}
     */
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

  /**
   * Creates a list with exactly one element.
   *
   * @param a the element, which may be {@code null}
   * @param <A> the element type
   * @return a list containing {@code a}
   */
  static <A> NonEmptyList<A> single(A a) {
    return new Single<>(a, 1);
  }

  /**
   * Creates a list from a head element and a tail.
   *
   * @param head the first element, which may be {@code null}
   * @param tail the remaining elements
   * @param <A> the element type
   * @return a list consisting of {@code head} followed by the elements of {@code tail}
   * @throws NullPointerException if {@code tail} is {@code null}
   */
  static <A> NonEmptyList<A> cons(A head, NonEmptyList<? extends A> tail) {
    Objects.requireNonNull(tail);
    return new Cons<>(head, narrow(tail), 1 + tail.size());
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0) {
    return single(a0);
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param a1 the second element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0, A a1) {
    return cons(a0, single(a1));
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param a1 the second element
   * @param a2 the third element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0, A a1, A a2) {
    return cons(a0, cons(a1, single(a2)));
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param a1 the second element
   * @param a2 the third element
   * @param a3 the fourth element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3) {
    return cons(a0, cons(a1, cons(a2, single(a3))));
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param a1 the second element
   * @param a2 the third element
   * @param a3 the fourth element
   * @param a4 the fifth element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4) {
    return cons(a0, cons(a1, cons(a2, cons(a3, single(a4)))));
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param a1 the second element
   * @param a2 the third element
   * @param a3 the fourth element
   * @param a4 the fifth element
   * @param a5 the sixth element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4, A a5) {
    return cons(a0, cons(a1, cons(a2, cons(a3, cons(a4, single(a5))))));
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param a1 the second element
   * @param a2 the third element
   * @param a3 the fourth element
   * @param a4 the fifth element
   * @param a5 the sixth element
   * @param a6 the seventh element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4, A a5, A a6) {
    return cons(a0, cons(a1, cons(a2, cons(a3, cons(a4, cons(a5, single(a6)))))));
  }

  /**
   * Creates a list from the given elements, in order.
   *
   * @param a0 the first element
   * @param a1 the second element
   * @param a2 the third element
   * @param a3 the fourth element
   * @param a4 the fifth element
   * @param a5 the sixth element
   * @param a6 the seventh element
   * @param a7 the eighth element
   * @param <A> the element type
   * @return a list containing the given elements
   */
  static <A> NonEmptyList<A> of(A a0, A a1, A a2, A a3, A a4, A a5, A a6, A a7) {
    return cons(a0, cons(a1, cons(a2, cons(a3, cons(a4, cons(a5, cons(a6, single(a7))))))));
  }

  /**
   * Creates a list from the given elements, in order. {@code as} may be empty, in which case the
   * list contains only {@code a}.
   *
   * @param a the first element
   * @param as the remaining elements
   * @param <A> the element type
   * @return a list containing {@code a} followed by the elements of {@code as}
   * @throws NullPointerException if {@code as} is {@code null}
   */
  @SafeVarargs
  static <A> NonEmptyList<A> of(A a, A... as) {
    Objects.requireNonNull(as);
    if (as.length == 0) {
      return single(a);
    }
    var result = single(as[as.length - 1]);
    for (int i = as.length - 2; i >= 0; i--) {
      result = result.prepend(as[i]);
    }
    result = result.prepend(a);
    return result;
  }

  /**
   * Creates a list from the elements of an iterable. A {@code NonEmptyList} is returned as is.
   *
   * @param as the elements
   * @param <A> the element type
   * @return a {@code Some} holding the elements in iteration order, or {@code None} if {@code as}
   *     is empty
   * @throws NullPointerException if {@code as} is {@code null}
   */
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

  /**
   * Creates a list from the elements of a stream. The stream is consumed.
   *
   * @param as the elements
   * @param <A> the element type
   * @return a {@code Some} holding the elements in encounter order, or {@code None} if {@code as}
   *     is empty
   * @throws NullPointerException if {@code as} is {@code null}
   */
  static <A> Option<NonEmptyList<A>> of(Stream<? extends A> as) {
    Objects.requireNonNull(as);
    return of(as.iterator());
  }

  /**
   * Creates a list from the elements of an iterator. The iterator is consumed.
   *
   * @param iterator the elements
   * @param <A> the element type
   * @return a {@code Some} holding the elements in iteration order, or {@code None} if the iterator
   *     has no elements
   * @throws NullPointerException if {@code iterator} is {@code null}
   */
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

  /**
   * Appends another list to this one. The elements of this list are copied, so the cost is
   * proportional to the size of this list.
   *
   * @param other the list to append
   * @return a list containing the elements of this list followed by those of {@code other}
   * @throws NullPointerException if {@code other} is {@code null}
   */
  default NonEmptyList<A> concat(NonEmptyList<? extends A> other) {
    Objects.requireNonNull(other);
    return foldRight(narrow(other), NonEmptyList::cons);
  }

  /**
   * Reverses this list.
   *
   * @return a list containing the elements of this list in reverse order
   */
  default NonEmptyList<A> reverse() {
    var it = this.iterator();
    var result = NonEmptyList.single(it.next());
    while (it.hasNext()) {
      result = result.prepend(it.next());
    }
    return result;
  }

  /**
   * Adds an element at the front. This is cheap, as the existing list is shared.
   *
   * @param a the element to add, which may be {@code null}
   * @return a list consisting of {@code a} followed by the elements of this list
   */
  default NonEmptyList<A> prepend(A a) {
    return cons(a, this);
  }

  /**
   * Adds an element at the end. The elements of this list are copied, so the cost is proportional
   * to its size.
   *
   * @param a the element to add, which may be {@code null}
   * @return a list containing the elements of this list followed by {@code a}
   */
  default NonEmptyList<A> append(A a) {
    return this.concat(single(a));
  }

  /**
   * Returns the first element.
   *
   * @return the first element, which may be {@code null}
   */
  A head();

  /**
   * Returns the first element as an {@link Option}. As the list is never empty, the result is
   * always a {@code Some}.
   *
   * @return a {@code Some} holding the first element
   */
  default Option<A> headOption() {
    return Option.some(head());
  }

  /**
   * Returns the elements after the first, if there are any.
   *
   * @return a {@code Some} holding the remaining elements, or {@code None} if this list has only
   *     one element
   */
  Option<NonEmptyList<A>> tailOption();

  /**
   * Returns the elements after the first.
   *
   * @return the remaining elements
   * @throws NoSuchElementException if this list has only one element
   */
  NonEmptyList<A> tail();

  /**
   * Returns the number of elements.
   *
   * @return the number of elements, which is at least 1
   */
  int size();

  /**
   * Checks whether this list is empty.
   *
   * @return always {@code false}
   */
  default boolean isEmpty() {
    return false;
  }

  /**
   * Checks whether this list is non-empty.
   *
   * @return always {@code true}
   */
  default boolean nonEmpty() {
    return true;
  }

  /**
   * Checks whether this list contains the given element.
   *
   * @param elem the element to look for, which may be {@code null}
   * @return {@code true} if an element equals {@code elem}, {@code false} otherwise
   */
  default boolean contains(A elem) {
    return exists(a -> Objects.equals(a, elem));
  }

  /**
   * Checks whether any element satisfies the given predicate.
   *
   * @param p the predicate to test the elements with
   * @return {@code true} if at least one element satisfies {@code p}, {@code false} otherwise
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default boolean exists(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    for (A value : this) {
      if (p.test(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Finds the first element that satisfies the given predicate.
   *
   * @param p the predicate to test the elements with
   * @return a {@code Some} holding the first matching element, or {@code None} if there is none
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default Option<A> find(Predicate<? super A> p) {
    Objects.requireNonNull(p);

    for (A value : this) {
      if (p.test(value)) {
        return Option.some(value);
      }
    }

    return Option.none();
  }

  /**
   * Keeps the elements that satisfy the given predicate.
   *
   * @param p the predicate to test the elements with
   * @return a {@code Some} holding the matching elements in order, or {@code None} if no element
   *     matches
   * @throws NullPointerException if {@code p} is {@code null}
   */
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

  /**
   * Copies the elements into a new, mutable {@link java.util.List}.
   *
   * @return a new {@code ArrayList} containing the elements in order
   */
  default List<A> toList() {
    final var result = new ArrayList<A>(this.size());
    this.forEach(result::add);
    return result;
  }

  /**
   * Applies a function to each element.
   *
   * @param f the function to apply to each element
   * @param <B> the type of the mapped elements
   * @return a list containing the results of {@code f}, in order
   * @throws NullPointerException if {@code f} is {@code null}
   */
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

  /**
   * Applies a list-returning function to each element and concatenates the results.
   *
   * @param f the function to apply to each element
   * @param <B> the type of the elements of the result
   * @return the concatenation of the results of {@code f}, in order
   * @throws NullPointerException if {@code f} is {@code null} or returns {@code null}
   */
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

  /**
   * Combines the elements from left to right, starting with the given value.
   *
   * @param zero the initial value
   * @param f combines the result so far with the next element
   * @param <B> the result type
   * @return the combined result
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <B> B foldLeft(B zero, Function2<? super B, ? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    B result = zero;
    for (A a : this) {
      result = f.apply(result, a);
    }
    return result;
  }

  /**
   * Combines the elements from right to left, starting with the given value.
   *
   * @param zero the initial value
   * @param f combines the next element with the result so far
   * @param <B> the result type
   * @return the combined result
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <B> B foldRight(B zero, Function2<? super A, ? super B, ? extends B> f) {
    Objects.requireNonNull(f);
    return this.reverse().foldLeft(zero, f.reversed());
  }

  /**
   * Combines all elements from left to right. No initial value is needed because the list is never
   * empty.
   *
   * @param r combines the result so far with the next element
   * @return the combined result
   * @throws NullPointerException if {@code r} is {@code null}
   */
  default A reduce(Function2<? super A, ? super A, ? extends A> r) {
    Objects.requireNonNull(r);
    return reduceMapLeft(Function1.identity(), r);
  }

  /**
   * Combines all elements from right to left. No initial value is needed because the list is never
   * empty.
   *
   * @param r combines the next element with the result so far
   * @return the combined result
   * @throws NullPointerException if {@code r} is {@code null}
   */
  default A reduceRight(Function2<? super A, ? super A, ? extends A> r) {
    Objects.requireNonNull(r);
    return reduceMapRight(Function1.identity(), r);
  }

  /**
   * Maps the first element with {@code map}, then combines the result with the remaining elements
   * from left to right.
   *
   * @param map converts the first element to the result type
   * @param r combines the result so far with the next element
   * @param <B> the result type
   * @return the combined result
   * @throws NullPointerException if {@code map} or {@code r} is {@code null}
   */
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

  /**
   * Maps the last element with {@code map}, then combines the result with the remaining elements
   * from right to left.
   *
   * @param map converts the last element to the result type
   * @param r combines the next element with the result so far
   * @param <B> the result type
   * @return the combined result
   * @throws NullPointerException if {@code map} or {@code r} is {@code null}
   */
  default <B> B reduceMapRight(
      Function1<? super A, ? extends B> map, Function2<? super A, ? super B, ? extends B> r) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(r);
    return this.reverse().reduceMapLeft(map, r.reversed());
  }

  /**
   * Joins the string representations of the elements without a separator.
   *
   * @return the joined string
   */
  default String mkString() {
    return mkString("");
  }

  /**
   * Joins the string representations of the elements with the given separator.
   *
   * @param sep the separator
   * @return the joined string
   * @throws NullPointerException if {@code sep} is {@code null}
   */
  default String mkString(String sep) {
    return mkString("", sep, "");
  }

  /**
   * Joins the string representations of the elements with the given separator, surrounded by a
   * start and an end string.
   *
   * @param start the string put in front of the elements
   * @param sep the separator between the elements
   * @param end the string put after the elements
   * @return the joined string
   * @throws NullPointerException if {@code start}, {@code sep} or {@code end} is {@code null}
   */
  default String mkString(String start, String sep, String end) {
    Objects.requireNonNull(start);
    Objects.requireNonNull(sep);
    Objects.requireNonNull(end);
    return start + reduceMapLeft(a -> "" + a, (str, a) -> str + sep + a) + end;
  }

  /**
   * Iterates over the elements in order.
   *
   * @return an iterator over the elements
   */
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
