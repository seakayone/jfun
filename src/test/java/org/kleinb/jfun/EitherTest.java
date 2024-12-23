package org.kleinb.jfun;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


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

}