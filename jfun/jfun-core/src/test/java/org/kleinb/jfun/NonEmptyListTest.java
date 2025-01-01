package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class NonEmptyListTest {

  // constructors

  @Test
  void singleConstructor() {
    var nel = new NonEmptyList.Single<>(1, 1);
    assertThat(nel).containsExactly(1);
  }

  @Test
  void singleConstructorNullIsAllowed() {
    assertThat(new NonEmptyList.Single<>(null, 1).head()).isNull();
  }

  @Test
  void singleConstructorSizeMustBeOne() {
    assertThatThrownBy(() -> new NonEmptyList.Single<>(1, 2))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void consConstructor() {
    var nel = new NonEmptyList.Cons<>(1, NonEmptyList.of(2), 2);
    assertThat(nel).containsExactly(1, 2);
  }

  @Test
  void consConstructorNullIsAllowedHead() {
    assertThat(new NonEmptyList.Cons<>(null, NonEmptyList.of(1), 2).head()).isNull();
  }

  @Test
  void consConstructorNullIsNotAllowedTail() {
    assertThatThrownBy(() -> new NonEmptyList.Cons<>(1, null, 2))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void consConstructorSizeMustBeOnePlusTailSize() {
    assertThatThrownBy(() -> new NonEmptyList.Cons<>(1, NonEmptyList.of(2), 3))
        .isInstanceOf(IllegalArgumentException.class);
  }

  // of

  @Test
  void ofValue() {
    var nel = NonEmptyList.of(1);
    assertThat(nel).containsExactly(1);
  }

  @Test
  void ofNullIsAllowed() {
    assertThat(NonEmptyList.of((Integer) null).head()).isNull();
  }

  @Test
  void ofVararg() {
    assertThat(NonEmptyList.of(1, 2, 3, 4)).containsExactly(1, 2, 3, 4);
  }

  @Test
  void ofIterableNonEmpty() {
    assertThat(NonEmptyList.of(List.of(1, 2, 3, 4)).get()).containsExactly(1, 2, 3, 4).hasSize(4);
    assertThat(NonEmptyList.of(Set.of(1, 2, 3, 4)).get())
        .containsExactlyInAnyOrder(1, 2, 3, 4)
        .hasSize(4);
  }

  @Test
  void ofIterableEmpty() {
    assertThat(NonEmptyList.of(List.of())).isEqualTo(Option.none());
    assertThat(NonEmptyList.of(Set.of())).isEqualTo(Option.none());
  }

  @Test
  void ofIterableNonEmptyList() {
    final var nel = NonEmptyList.of(1);
    final var actual = NonEmptyList.of(nel).get();
    assertThat(actual).isSameAs(nel);
    assertThat(nel.size()).isEqualTo(1);
  }

  @Test
  void ofStreamSingle() {
    NonEmptyList<Integer> actual = NonEmptyList.of(Stream.of(1)).get();
    assertThat(actual).containsExactly(1).hasSize(1);
  }

  @Test
  void ofStreamMulti() {
    NonEmptyList<Integer> actual = NonEmptyList.of(Stream.of(1, 2, 3)).get();
    assertThat(actual).containsExactly(1, 2, 3).hasSize(3);
  }

  @Test
  void ofStreamEmpty() {
    assertThat(NonEmptyList.of(Stream.empty())).isEqualTo(Option.none());
  }

  // .head .headOption

  @Test
  void headSingle() {
    var nel = NonEmptyList.of(1);
    assertThat(nel.head()).isEqualTo(1);
    assertThat(nel.headOption()).isEqualTo(Option.some(1));
  }

  @Test
  void headCons() {
    var nel = NonEmptyList.of(1, 2, 3);
    assertThat(nel.head()).isEqualTo(1);
    assertThat(nel.headOption()).isEqualTo(Option.some(1));
  }

  // .tail .tailOption

  @Test
  void tailSingle() {
    var nel = NonEmptyList.of(1);
    assertThat(nel.tailOption()).isEqualTo(Option.none());
    assertThatThrownBy(nel::tail)
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("Single does not have a tail");
  }

  @Test
  void tailCons() {
    var nel = NonEmptyList.of(1, 2, 3);
    assertThat(nel.tailOption()).isEqualTo(Option.some(NonEmptyList.of(2, 3)));
    assertThat(nel.tail()).isEqualTo(NonEmptyList.of(2, 3));
  }

  // .concat

  @Test
  void concat() {
    var nel = NonEmptyList.of(1, 2, 3).concat(NonEmptyList.of(4, 5, 6));
    assertThat(nel).containsExactly(1, 2, 3, 4, 5, 6);
  }

  // .isEmpty .nonEmpty

  @Test
  void singleNonEmptyIsEmpty() {
    var nel = NonEmptyList.of(1);
    assertThat(nel.nonEmpty()).isTrue();
    assertThat(nel.isEmpty()).isFalse();
  }

  @Test
  void consNonEmptyIsEmpty() {
    var nel = NonEmptyList.of(1, 2, 3);
    assertThat(nel.nonEmpty()).isTrue();
    assertThat(nel.isEmpty()).isFalse();
  }

  // size

  @Test
  void testSize() {
    var nel = NonEmptyList.of(1, 2, 3);
    assertThat(nel.size()).isEqualTo(3);
  }

  // .prepend

  @Test
  void prependSingle() {
    var nel = NonEmptyList.of(1).prepend(2);
    assertThat(nel).containsExactly(2, 1);
  }

  @Test
  void prependNullIsAllowed() {
    assertThat(NonEmptyList.of(1).prepend(null)).isEqualTo(NonEmptyList.of(null, 1));
  }

  // .append

  @Test
  void appendValue() {
    var nel = NonEmptyList.of(1, 2).append(3);
    assertThat(nel).containsExactly(1, 2, 3);
  }

  @Test
  void appendNullIsAllowed() {
    assertThat(NonEmptyList.of(1).append(null)).containsExactly(1, null);
  }

  // .contains

  @Test
  void singleContains() {
    var nel = NonEmptyList.of(1);
    assertThat(nel.contains(1)).isTrue();
    assertThat(nel.contains(2)).isFalse();
  }

  @Test
  void consContains() {
    var nel = NonEmptyList.of(1, 2, 3);
    assertThat(nel.contains(1)).isTrue();
    assertThat(nel.contains(2)).isTrue();
    assertThat(nel.contains(3)).isTrue();
    assertThat(nel.contains(4)).isFalse();
  }

  // .find

  @Test
  void find() {
    var nel = NonEmptyList.of(1, 3, 7);
    assertThat(nel.find(i -> i == 1)).isEqualTo(Option.some(1));
    assertThat(nel.find(i -> i == 2)).isEqualTo(Option.none());
  }

  // .filter

  @Test
  void filter() {
    var nel = NonEmptyList.of(1, 2, 3, 4, 5, 6, 7);
    assertThat(nel.filter(i -> i % 2 == 0)).isEqualTo(Option.some(NonEmptyList.of(2, 4, 6)));
    assertThat(nel.filter(i -> i > 100)).isEqualTo(Option.none());
  }

  // .reverse

  @Test
  void reverseSingle() {
    assertThat(NonEmptyList.of(1).reverse()).containsExactly(1);
  }

  @Test
  void reverseMultiple() {
    assertThat(NonEmptyList.of(1, 2, 3).reverse()).containsExactly(3, 2, 1);
  }

  // .map

  @Test
  void mapSingle() {
    var nel = NonEmptyList.of(1).map(i -> i + 1);
    assertThat(nel).containsExactly(2);
  }

  @Test
  void mapNullSingle() {
    var nel = NonEmptyList.of(1);
    assertThat(nel.map(_ -> null)).isEqualTo(NonEmptyList.single(null));
  }

  @Test
  void mapMultiple() {
    var nel = NonEmptyList.of(1, 2, 3).map(i -> i * 2);
    assertThat(nel).containsExactly(2, 4, 6);
  }

  @Test
  void mapNullMultiple() {
    var nel = NonEmptyList.of(1, 2, 3);
    assertThat(nel.map(_ -> null)).isEqualTo(NonEmptyList.of(null, null, null));
  }

  // .iterator

  @Test
  void singleNextNextThrows() {
    var nel = NonEmptyList.of(1);
    var iterator = nel.iterator();
    assertThat(iterator.next()).isEqualTo(1);
    assertThatThrownBy(iterator::next)
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("No more elements");
  }

  // .toList

  @Test
  void toListSingle() {
    final List<Integer> actual = NonEmptyList.of(1).toList();
    assertThat(actual).containsExactly(1);
  }

  @Test
  void toListMultiple() {
    final List<Integer> actual = NonEmptyList.of(1, 2, 3).toList();
    assertThat(actual).containsExactly(1, 2, 3);
  }

  // .flatMap

  @Test
  void flatMapSingle() {
    var nel = NonEmptyList.of(1).flatMap(i -> NonEmptyList.of(i + 1));
    assertThat(nel).containsExactly(2);
  }

  @Test
  void flatMapNullSingle() {
    var nel = NonEmptyList.of(1);
    assertThatThrownBy(() -> nel.flatMap(_ -> null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void flatMapSingleMultiple() {
    var nel = NonEmptyList.of(1).flatMap(i -> NonEmptyList.of(i + 1, i + 2));
    assertThat(nel).containsExactly(2, 3);
  }

  @Test
  void flatMapConsMultiple() {
    var nel = NonEmptyList.of(1, 10).flatMap(i -> NonEmptyList.of(i + 1, i + 2));
    assertThat(nel).containsExactly(2, 3, 11, 12);
  }

  @Test
  void flatMapNullMulti() {
    var nel = NonEmptyList.of(1);
    assertThatThrownBy(() -> nel.flatMap(_ -> null)).isInstanceOf(NullPointerException.class);
  }

  // .foldLeft

  @Test
  void foldLeftSingle() {
    var nel = NonEmptyList.of(1);
    var actual = nel.foldLeft(0, Integer::sum);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void foldLeftMultiple() {
    var nel = NonEmptyList.of("1", "2", "3");
    var actual = nel.foldLeft("", String::concat);
    assertThat(actual).isEqualTo("123");
  }

  @Test
  void foldRightSingle() {
    var nel = NonEmptyList.of(1);
    var actual = nel.foldRight(0, Integer::sum);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void foldRightMultiple() {
    var nel = NonEmptyList.of("1", "2", "3");
    var actual = nel.foldRight("", String::concat);
    assertThat(actual).isEqualTo("123");
  }

  // .reduceMapLeft

  @Test
  void reduceMapLeftSingle() {
    var nel = NonEmptyList.of(1);
    var actual = nel.reduceMapLeft(i -> i * 2, Integer::sum);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  void reduceMapLeftMultiple() {
    var nel = NonEmptyList.of("1", "2", "3");
    var actual = nel.reduceMapLeft(Function1.identity(), String::concat);
    assertThat(actual).isEqualTo("123");
  }

  // .reduceMapRight

  @Test
  void reduceMapRightSingle() {
    var nel = NonEmptyList.of(1);
    var actual = nel.reduceMapRight(i -> i * 2, Integer::sum);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  void reduceMapRightMultiple() {
    var nel = NonEmptyList.of("1", "2", "3");
    var actual = nel.reduceMapRight(Function1.identity(), String::concat);
    assertThat(actual).isEqualTo("123");
  }

  // .reduce

  @Test
  void reduceSingle() {
    var nel = NonEmptyList.of(1);
    var actual = nel.reduce(Integer::sum);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void reduceMultiple() {
    var nel = NonEmptyList.of("1", "2", "3");
    var actual = nel.reduce(String::concat);
    assertThat(actual).isEqualTo("123");
  }

  // .reduceRight

  @Test
  void reduceRightSingle() {
    var nel = NonEmptyList.of(1);
    var actual = nel.reduceRight(Integer::sum);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void reduceRightMultiple() {
    var nel = NonEmptyList.of("1", "2", "3");
    var actual = nel.reduceRight(String::concat);
    assertThat(actual).isEqualTo("123");
  }

  // .mkString

  @Test
  void mkString() {
    var nel = NonEmptyList.of(1, 2, 3);
    var actual = nel.mkString();
    assertThat(actual).isEqualTo("123");
  }

  @Test
  void mkStringSep() {
    var nel = NonEmptyList.of(1, 2, 3);
    var actual = nel.mkString(",");
    assertThat(actual).isEqualTo("1,2,3");
  }

  @Test
  void mkStringStartSepEnd() {
    var nel = NonEmptyList.of(1, 2, 3);
    var actual = nel.mkString("[", ", ", "]");
    assertThat(actual).isEqualTo("[1, 2, 3]");
  }

  @Test
  void mkStringIsNullSafe() {
    var nel = NonEmptyList.of(null, null, 3);
    var actual = nel.mkString("[", ", ", "]");
    assertThat(actual).isEqualTo("[null, null, 3]");
  }

  // .toString

  @Test
  void singleToString() {
    var nel = NonEmptyList.of(1);
    assertThat(nel.toString()).isEqualTo("NonEmptyList(1)");
  }

  @Test
  void consToString() {
    var nel = NonEmptyList.of(1, 2, 3);
    assertThat(nel.toString()).isEqualTo("NonEmptyList(1, 2, 3)");
  }

  // collect from Stream

  @Test
  void collectFromEmptyStream() {
    var empty = Stream.<Integer>empty();
    assertThat(empty.collect(NonEmptyList.collector())).isEqualTo(Option.none());
  }

  @Test
  void collectFromSingleElementStream() {
    var single = Stream.of(1);
    assertThat(single.collect(NonEmptyList.collector())).isEqualTo(Option.some(NonEmptyList.of(1)));
  }

  @Test
  void collectFromMultiElementStream() {
    var multi = Stream.of(1, 2, 3, null);
    assertThat(multi.collect(NonEmptyList.collector()))
        .isEqualTo(Option.some(NonEmptyList.of(1, 2, 3, null)));
  }

  @Test
  void collectFromNullElementStreamParallel() {
    var actual = integerStream().parallel().collect(NonEmptyList.collector()).get();
    assertThat(actual).containsExactlyInAnyOrderElementsOf(integerStream().toList());
  }

  private Stream<Integer> integerStream() {
    var atomicInt = new AtomicInteger();
    return Stream.generate(atomicInt::getAndIncrement).takeWhile(i -> i < 10_000);
  }
}
