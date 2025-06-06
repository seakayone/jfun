package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Either.Left;
import org.kleinb.jfun.Either.Right;

class EitherTest {

  // factory methods, construction

  @Test
  void shouldCreateLeft() {
    Either<Integer, String> actual = Either.left(42);
    assertThat(actual).isInstanceOf(Left.class);
  }

  @Test
  void shouldCreateRight() {
    Either<Integer, String> actual = Either.right("42");
    assertThat(actual).isInstanceOf(Right.class);
  }

  // .isLeft

  @Test
  void shouldReturnTrueForLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.isLeft()).isTrue();
  }

  @Test
  void shouldReturnFalseForRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.isLeft()).isFalse();
  }

  // .isRight

  @Test
  void shouldReturnTrueForRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.isRight()).isTrue();
  }

  @Test
  void shouldReturnFalseForLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.isRight()).isFalse();
  }

  // .swap

  @Test
  void shouldSwapLeftToRight() {
    assertThat(Either.left(42).swap()).isEqualTo(Either.right(42));
  }

  @Test
  void shouldSwapRightToLeft() {
    assertThat(Either.right(42).swap()).isEqualTo(Either.left(42));
  }

  // .contains

  @Test
  void shouldContainRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.contains("42")).isTrue();
  }

  @Test
  void shouldNotContainLeft() {
    Either<String, String> either = Either.left("42");
    assertThat(either.contains("42")).isFalse();
  }

  @Test
  void shouldNotContainRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.contains("43")).isFalse();
  }

  // .exists

  @Test
  void shouldExistRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.exists(_ -> true)).isTrue();
  }

  @Test
  void shouldNotExistRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.exists(_ -> false)).isFalse();
  }

  @Test
  void shouldNotExistLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.exists(_ -> true)).isFalse();
  }

  // .map

  @Test
  void shouldMapRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.map(Integer::parseInt)).isEqualTo(Either.right(42));
  }

  @Test
  void shouldMapLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.map(Integer::parseInt)).isEqualTo(Either.left(42));
  }

  // .mapLeft

  @Test
  void shouldMapLeftRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.mapLeft(_ -> "not mapped")).isEqualTo(Either.right("42"));
  }

  @Test
  void shouldMapLeftLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.mapLeft(i -> i + 1)).isEqualTo(Either.left(43));
  }

  // .bimap

  @Test
  void shouldBimapRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.bimap(Function.identity(), Integer::parseInt)).isEqualTo(Either.right(42));
  }

  @Test
  void shouldBimapLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.bimap(Function.identity(), Integer::parseInt)).isEqualTo(Either.left(42));
  }

  // .flatMap
  @Test
  void shouldFlatMapRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.flatMap(s -> Either.right(Integer.parseInt(s)))).isEqualTo(Either.right(42));
  }

  @Test
  void shouldFlatMapLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.flatMap(s -> Either.right("ignored"))).isEqualTo(Either.left(42));
  }

  @Test
  void shouldFlatMapLeftToRight() {
    Either<String, Integer> either = Either.right(42);
    assertThat(either.flatMap(i -> Either.left(String.valueOf(i)))).isEqualTo(Either.left("42"));
  }

  // .get

  @Test
  void shouldGetRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.get()).isEqualTo("42");
  }

  @Test
  void shouldGetLeftOnRightThrows() {
    Either<Integer, String> either = Either.right("42");
    assertThatThrownBy(either::getLeft).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldGetOnLeftThrows() {
    Either<Integer, String> either = Either.left(42);
    assertThatThrownBy(either::get).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldGetLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThatThrownBy(either::get).isInstanceOf(NoSuchElementException.class);
  }

  // .getOrElse

  @Test
  void shouldGetRightOrElse() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.getOrElse("0")).isEqualTo("42");
  }

  @Test
  void shouldGetLeftOrElse() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.getOrElse("0")).isEqualTo("0");
  }

  // .orElse

  @Test
  void shouldReturnRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.orElse(() -> Either.right("0"))).isEqualTo(Either.right("42"));
  }

  @Test
  void shouldReturnAlternative() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.orElse(() -> Either.right("0"))).isEqualTo(Either.right("0"));
  }

  // .fold

  @Test
  void shouldFoldRight() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.fold(Function.identity(), Integer::valueOf)).isEqualTo(42);
  }

  @Test
  void shouldFoldLeft() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.fold(Function.identity(), Integer::valueOf)).isEqualTo(42);
  }

  // filterToOption

  @Test
  void shouldFilterRightToSome() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.filterToOption(s -> s.equals("42"))).isEqualTo(Option.some("42"));
  }

  @Test
  void shouldFilterRightToNone() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.filterToOption(s -> s.equals("43"))).isEqualTo(Option.none());
  }

  @Test
  void shouldFilterLeftToNone() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.filterToOption(s -> s.equals("42"))).isEqualTo(Option.none());
  }

  // filterOrElse

  @Test
  void shouldFilterRightOrElsePredicateTrue() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.filterOrElse(s -> s.equals("42"), "0")).isEqualTo("42");
  }

  @Test
  void shouldFilterRightOrElsePredicateFalse() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.filterOrElse(s -> s.equals("0"), "0")).isEqualTo("0");
  }

  @Test
  void shouldFilterLeftOrElse() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.filterOrElse(s -> s.equals("42"), "0")).isEqualTo("0");
  }

  // .tap

  @Test
  void shouldTapRight() {
    StringBuilder sb = new StringBuilder();
    Either<Object, String> actual = Either.right("42").tap(sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(Either.right("42"));
  }

  @Test
  void shouldNotTapLeft() {
    StringBuilder sb = new StringBuilder();
    Either<Integer, String> actual = Either.<Integer, String>left(42).tap(sb::append);
    assertThat(sb.toString()).isEmpty();
    assertThat(actual).isEqualTo(Either.left(42));
  }

  // .tapLeft

  @Test
  void shouldTapLeft() {
    StringBuilder sb = new StringBuilder();
    Either<Integer, String> actual = Either.<Integer, String>left(42).tapLeft(sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(Either.left(42));
  }

  @Test
  void shouldNotTapRight() {
    StringBuilder sb = new StringBuilder();
    Either<Integer, String> actual = Either.<Integer, String>right("42").tapLeft(sb::append);
    assertThat(sb.toString()).isEmpty();
    assertThat(actual).isEqualTo(Either.right("42"));
  }

  // .tapBoth

  @Test
  void shouldTapBothRight() {
    StringBuilder sb = new StringBuilder();
    Either<Integer, String> actual =
        Either.<Integer, String>right("42").tapBoth(sb::append, sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(Either.right("42"));
  }

  @Test
  void shouldTapBothLeft() {
    StringBuilder sb = new StringBuilder();
    Either<Integer, String> actual =
        Either.<Integer, String>left(42).tapBoth(sb::append, sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(Either.left(42));
  }

  // conversion methods

  // .toOption

  @Test
  void shouldConvertRightToSome() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.toOption()).isEqualTo(Option.some("42"));
  }

  @Test
  void shouldConvertLeftToNone() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.toOption()).isEqualTo(Option.none());
  }

  // .toOptional

  @Test
  void shouldConvertRightToOptional() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.toOptional()).isEqualTo(java.util.Optional.of("42"));
  }

  @Test
  void shouldConvertLeftToEmptyOptional() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.toOptional()).isEqualTo(java.util.Optional.empty());
  }

  // .toList

  @Test
  void shouldConvertRightToList() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.toList()).isEqualTo(List.of("42"));
  }

  @Test
  void shouldConvertLeftToList() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.toList()).isEqualTo(List.of());
  }

  // .toTry

  @Test
  void shouldConvertRightToSuccess() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.toTry(i -> new RuntimeException("Failed with " + i)))
        .isEqualTo(Try.success("42"));
  }

  @Test
  void shouldConvertLeftToFailure() {
    Either<Integer, String> either = Either.left(42);
    RuntimeException ex = new RuntimeException("Failed");
    Try<String> actual = either.toTry(_ -> ex);
    assertThat(actual.isFailure()).isTrue();
    assertThat(actual.getFailure()).isSameAs(ex);
  }

  // .toValidation

  @Test
  void shouldConvertRightToValid() {
    Either<Integer, String> either = Either.right("42");
    assertThat(either.toValidation()).isEqualTo(Validation.valid("42"));
  }

  @Test
  void shouldConvertLeftToInvalid() {
    Either<Integer, String> either = Either.left(42);
    assertThat(either.toValidation()).isEqualTo(Validation.invalid(42));
  }

  // .iterator

  @Test
  void shouldConvertRightToIterator() {
    Either<Integer, String> either = Either.right("42");
    Iterator<String> iterator = either.iterator();
    assertThat(iterator.next()).isEqualTo("42");
    assertThat(iterator.hasNext()).isFalse();
    assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  void shouldConvertLeftToEmptyIterator() {
    Either<Integer, String> either = Either.left(42);
    Iterator<String> iterator = either.iterator();
    assertThat(iterator.hasNext()).isFalse();
    assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
  }
}
