package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OptionTest {

  // factory methods; construction
  @Test
  void shouldCreateSome() {
    Option<Integer> actual = Option.some(42);
    assertThat(actual.get()).isEqualTo(42);
    assertThat(actual).isInstanceOf(Some.class);
  }

  @Test
  void shouldCreateSomeWithNull() {
    Option<Integer> actual = Option.some(null);
    assertThat(actual.get()).isNull();
    assertThat(actual).isInstanceOf(Some.class);
  }

  @Test
  void shouldCreateNone() {
    Option<Integer> actual = Option.none();
    assertThat(actual).isEqualTo(Option.none());
    assertThat(actual).isInstanceOf(None.class);
  }

  @Test
  void shouldMapNullToNone() {
    Option<?> option = Option.of(null);
    assertThat(option).isEqualTo(Option.none());
  }

  @Test
  void shouldMapNonNullToSome() {
    assertThat(Option.of(new Object()).isSome()).isTrue();
  }

  @Test
  void shouldMapOptionalToSome() {
    assertThat(Option.ofOptional(Optional.of(42))).isEqualTo(Option.some(42));
  }

  @Test
  void shouldMapEmptyOptionalToNone() {
    assertThat(Option.ofOptional(Optional.empty())).isEqualTo(Option.none());
  }

  // .sequence

  @Test
  void shouldSequenceSome() {
    assertThat(Option.sequence(List.of(Option.some(42), Option.some(43))))
        .isEqualTo(Option.some(List.of(42, 43)));
  }

  @Test
  void shouldSequenceNone() {
    assertThat(Option.sequence(List.of(Option.some(42), Option.none()))).isEqualTo(Option.none());
  }

  // .get

  @Test
  void shouldGetSomeValue() {
    assertThat(Option.some(42).get()).isEqualTo(42);
  }

  @Test
  void shouldThrowOnGetNone() {
    assertThatThrownBy(Option.<Integer>none()::get).isInstanceOf(NoSuchElementException.class);
  }

  // .isSome , isNone

  @Test
  void shouldReturnForSome() {
    Option<Integer> actual = Option.some(42);
    assertThat(actual.isSome()).isTrue();
    assertThat(actual.isNone()).isFalse();
  }

  @Test
  void shouldReturnForNone() {
    Option<Integer> actual = Option.none();
    assertThat(actual.isSome()).isFalse();
    assertThat(actual.isNone()).isTrue();
  }

  // .map

  @Test
  void shouldMapSome() {
    assertThat(Option.some(42).map(String::valueOf)).isEqualTo(Option.of("42"));
  }

  @Test
  void shouldMapNone() {
    assertThat(Option.<Integer>none().map(Object::toString)).isEqualTo(Option.none());
  }

  @Test
  void shouldMapNullToSome() {
    assertThat(Option.some(42).map(i -> null)).isEqualTo(Option.some(null));
  }

  // .flatMap

  @Test
  void shouldFlatMapSome() {
    assertThat(Option.some(42).flatMap(i -> Option.of(String.valueOf(i))))
        .isEqualTo(Option.some("42"));
  }

  @Test
  void shouldFlatMapNone() {
    assertThat(Option.none().flatMap(i -> Option.some(String.valueOf(i)))).isEqualTo(Option.none());
  }

  @Test
  void shouldFlatMapSomeNone() {
    assertThat(Option.some(42).flatMap(i -> Option.none())).isEqualTo(Option.none());
  }

  // .fold

  @Test
  void shouldFoldSomeIntegerToString() {
    assertThat(Option.some(42).fold(() -> "none", Object::toString)).isEqualTo("42");
  }

  @Test
  void shouldFoldNone() {
    assertThat(Option.none().fold(() -> "none", Object::toString)).isEqualTo("none");
  }

  // .orNull

  @Test
  void shouldReturnSomeValue() {
    assertThat(Option.some(42).getOrNull()).isEqualTo(42);
  }

  @Test
  void shouldReturnNullForNone() {
    assertThat(Option.none().getOrNull()).isNull();
  }

  // .orElse

  @Test
  void shouldOrElseSome() {
    assertThat(Option.some(42).orElse(() -> Option.some(0))).isEqualTo(Option.some(42));
  }

  @Test
  void shouldOrElseNone() {
    assertThat(Option.none().orElse(() -> Option.some(42))).isEqualTo(Option.some(42));
  }

  @Test
  void orElseWithNoneAndNone() {
    assertThat(Option.none().orElse(Option::none)).isEqualTo(Option.none());
  }

  // .getOrElse
  @Test
  void shouldGetOrElseSomeValue() {
    assertThat(Option.some(42).getOrElse(0)).isEqualTo(42);
  }

  @Test
  void shouldGetOrElseNoneValue() {
    assertThat(Option.none().getOrElse(42)).isEqualTo(42);
  }

  // .contains
  @Test
  void shouldContainValue() {
    assertThat(Option.some(42).contains(42)).isTrue();
  }

  @Test
  void shouldNotContainValue() {
    assertThat(Option.some(0).contains(42)).isFalse();
  }

  @Test
  void shouldNotContainValueInNone() {
    assertThat(Option.none().contains(42)).isFalse();
  }

  // .exists
  @Test
  void shouldExist() {
    assertThat(Option.some(42).exists(i -> i == 42)).isTrue();
  }

  @Test
  void shouldNotExist() {
    assertThat(Option.some(42).exists(i -> i == 0)).isFalse();
  }

  @Test
  void shouldNotExistInNone() {
    assertThat(Option.none().exists(i -> i.equals(42))).isFalse();
  }

  // .filter
  @Test
  void shouldFilterSomeTruePredicate() {
    assertThat(Option.some(42).filter(i -> i == 42)).isEqualTo(Option.some(42));
  }

  @Test
  void shouldFilterSomeFalsePredicate() {
    assertThat(Option.some(42).filter(i -> i == 0)).isEqualTo(Option.none());
  }

  @Test
  void shouldFilterNone() {
    assertThat(Option.none().filter(i -> i.equals(42))).isEqualTo(Option.none());
  }

  // .filterNot
  @Test
  void shouldFilterNotSomeTruePredicate() {
    assertThat(Option.some(42).filterNot(i -> i == 0)).isEqualTo(Option.some(42));
  }

  @Test
  void shouldFilterNotSomeFalsePredicate() {
    assertThat(Option.some(42).filterNot(i -> i == 42)).isEqualTo(Option.none());
  }

  @Test
  void shouldFilterNotNone() {
    assertThat(Option.none().filterNot(i -> i.equals(42))).isEqualTo(Option.none());
  }

  // .tap

  @Test
  void shouldTapSome() {
    StringBuilder sb = new StringBuilder();
    Option<Integer> actual = Option.some(42).tap(sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(Option.some(42));
  }

  @Test
  void shouldNotTapNone() {
    StringBuilder sb = new StringBuilder();
    Option<Integer> actual = Option.<Integer>none().tap(sb::append);
    assertThat(sb.toString()).isEmpty();
    assertThat(actual).isEqualTo(Option.none());
  }

  // .tapNone

  @Test
  void shouldTapNone() {
    StringBuilder sb = new StringBuilder();
    Option<Integer> actual = Option.<Integer>none().tapNone(() -> sb.append("none"));
    assertThat(sb.toString()).isEqualTo("none");
    assertThat(actual).isEqualTo(Option.none());
  }

  @Test
  void shouldNotTapSome() {
    StringBuilder sb = new StringBuilder();
    Option<Integer> actual = Option.some(42).tapNone(() -> sb.append("none"));
    assertThat(sb.toString()).isEmpty();
    assertThat(actual).isEqualTo(Option.some(42));
  }

  // .tapBoth

  @Test
  void shouldTapBothSome() {
    StringBuilder sb = new StringBuilder();
    Option<Integer> actual = Option.some(42).tapBoth(() -> sb.append("none"), sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(Option.some(42));
  }

  @Test
  void shouldTapBothNone() {
    StringBuilder sb = new StringBuilder();
    Option<Integer> actual = Option.<Integer>none().tapBoth(() -> sb.append("none"), sb::append);
    assertThat(sb.toString()).isEqualTo("none");
    assertThat(actual).isEqualTo(Option.none());
  }

  // conversion methods

  // .toStream

  @Test
  void shouldConvertSomeToStream() {
    assertThat(Option.some(42).toStream()).containsExactly(42);
  }

  @Test
  void shouldConvertNoneToEmptyStream() {
    assertThat(Option.none().toStream()).isEmpty();
  }

  // .toList
  @Test
  void shouldConvertSomeToList() {
    assertThat(Option.some(42).toList()).isEqualTo(List.of(42));
  }

  @Test
  void shouldConvertNoneToList() {
    assertThat(Option.none().toList()).isEqualTo(List.of());
  }

  // .toOptional
  @Test
  void shouldConvertSomeToOptional() {
    assertThat(Option.some(42).toOptional()).isEqualTo(Optional.of(42));
  }

  @Test
  void shouldConvertNoneToOptional() {
    assertThat(Option.none().toOptional()).isEqualTo(Optional.empty());
  }

  // .toRight
  @Test
  void shouldConvertSomeToRight() {
    assertThat(Option.some(42).toRight(() -> "left")).isEqualTo(Either.right(42));
  }

  @Test
  void shouldConvertNoneToLeft() {
    assertThat(Option.none().toRight(() -> "left")).isEqualTo(Either.left("left"));
  }

  // .toTry
  @Test
  void shouldConvertSomeToTry() {
    assertThat(Option.some(42).toTry()).isEqualTo(Try.success(42));
  }

  @Test
  void shouldConvertNoneToFailure() {
    assertThat(Option.none().toTry())
        .has(new Condition<>(Try::isFailure, "is failure"))
        .has(
            new Condition<>(
                t -> t.getFailure() instanceof NoSuchElementException,
                "is NoSuchElementException"));
  }
}
