package org.kleinb.jfun;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TryTest {

    // factory method
    @Test
    void shouldCreateAFailure() {
        Try<Integer> actual = Try.of(() -> {
            throw new RuntimeException("Boom");
        });
        assertThat(actual.getFailure()).isInstanceOf(RuntimeException.class).hasMessage("Boom");
    }

    @Test
    void shouldCreateASuccess() {
        assertThat(Try.of(() -> 1)).isEqualTo(Try.success(1));
    }

    @Test
    void shouldRunSuccess() {
        Runnable noOp = () -> {
        };
        assertThat(Try.ofRunnable(noOp)).isEqualTo(Try.success(null));
    }

    @Test
    void shouldRunFailure() {
        Try<Void> actual = Try.ofRunnable(() -> {
            throw new RuntimeException("Boom");
        });
        assertThat(actual.getFailure()).isInstanceOf(RuntimeException.class).hasMessage("Boom");
    }

    @Test
    void shouldCreateASuccessFromSupplier() {
        assertThat(Try.ofSupplier(() -> 1)).isEqualTo(Try.success(1));
    }

    @Test
    void shouldCreateAFailureFromSupplier() {
        Try<Integer> actual = Try.ofSupplier(() -> {
            throw new RuntimeException("Boom");
        });
        assertThat(actual.getFailure()).isInstanceOf(RuntimeException.class).hasMessage("Boom");
    }

    @Test
    void shouldCreateASuccessFromCallable() {
        assertThat(Try.ofCallable(() -> 1)).isEqualTo(Try.success(1));
    }

    @Test
    void shouldCreateAFailureFromCallable() {
        Try<Integer> actual = Try.ofCallable(() -> {
            throw new RuntimeException("Boom");
        });
        assertThat(actual.getFailure()).isInstanceOf(RuntimeException.class).hasMessage("Boom");
    }

    // isSuccess, isFailure
    @Test
    void shouldReturnForSuccess() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.isSuccess()).isTrue();
        assertThat(actual.isFailure()).isFalse();
    }

    @Test
    void shouldReturnForFailure() {
        Try<Integer> actual = Try.failure(new RuntimeException("Boom"));
        assertThat(actual.isSuccess()).isFalse();
        assertThat(actual.isFailure()).isTrue();
    }

    // get

    @Test
    void shouldGetSuccessValue() {
        assertThat(Try.success(42).get()).isEqualTo(42);
    }

    @Test
    void shouldThrowOnGetFailure() {
        var exception = new Exception("Boom");
        assertThatThrownBy(() -> Try.failure(exception).get()).isEqualTo(exception);
    }

    // .contains

    @Test
    void shouldContainSuccess() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.contains(42)).isTrue();
    }

    @Test
    void shouldNotContainFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.failure(exception);
        assertThat(actual.contains(42)).isFalse();
    }

    // .exists

    @Test
    void shouldExistSuccess() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.exists(i -> i == 42)).isTrue();
    }

    @Test
    void shouldNotExistFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.failure(exception);
        assertThat(actual.exists(i -> i == 42)).isFalse();
    }

    // .getOrElse

    @Test
    void shouldGetSuccessValueOrElse() {
        assertThat(Try.success(42).getOrElse(0)).isEqualTo(42);
    }

    @Test
    void shouldGetFailureValueOrElse() {
        var exception = new Exception("Boom");
        assertThat(Try.failure(exception).getOrElse(0)).isEqualTo(0);
    }

    // .orElse

    @Test
    void shouldReturnSuccess() {
        Try<Integer> actual = Try.success(42).orElse(() -> Try.success(0));
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldReturnAlternative() {
        Try<Integer> actual = Try.<Integer>failure(new Exception("Boom")).orElse(() -> Try.success(0));
        assertThat(actual).isEqualTo(Try.success(0));
    }

    // .getFailure

    @Test
    void shouldGetFailure() {
        var exception = new Exception("Boom");
        assertThat(Try.failure(exception).getFailure()).isEqualTo(exception);
    }

    @Test
    void shouldThrowOnGetSuccess() {
        assertThatThrownBy(() -> Try.success(42).getFailure()).isInstanceOf(NoSuchElementException.class);
    }

    // .map

    @Test
    void shouldMapSuccess() {
        Try<Integer> actual = Try.success("42").map(Integer::parseInt);
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldMapFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).map(i -> i + 1);
        assertThat(actual).isEqualTo(Try.failure(exception));
    }

    @Test
    void shouldThrowIfMapFunctionThrows() {
        Try<Integer> actual = Try.success(42);
        assertThatThrownBy(() -> actual.map(_ -> {
            throw new RuntimeException();
        })).isInstanceOf(RuntimeException.class);
    }

    // .mapTry
    @Test
    void shouldMapTrySuccess() {
        Try<Integer> actual = Try.success("42").mapTry(Integer::parseInt);
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldMapTryFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).mapTry(i -> i + 1);
        assertThat(actual).isEqualTo(Try.failure(exception));
    }

    @Test
    void shouldMapTrySuccessToFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.success(42);
        assertThat(actual.mapTry(_ -> {
            throw exception;
        })).isEqualTo(Try.failure(exception));
    }

    // .flatMap
    @Test
    void shouldFlatMapSuccess() {
        Try<Integer> actual = Try.success("42").flatMap(_ -> Try.success(42));
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldFlatMapFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.<String>failure(exception).flatMap(_ -> Try.success(42));
        assertThat(actual).isEqualTo(Try.failure(exception));
    }

    @Test
    void shouldThrowIfFlatMapFunctionThrows() {
        Try<Integer> actual = Try.success(42);
        assertThatThrownBy(() -> actual.flatMap(_ -> {
            throw new RuntimeException();
        })).isInstanceOf(RuntimeException.class);
    }

    // .recover

    @Test
    void shouldRecoverFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).recover(_ -> 42);
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldNotRecoverSuccess() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.recover(_ -> 0)).isEqualTo(Try.success(42));
    }

    // .recoverWith

    @Test
    void shouldRecoverWithFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).recoverWith(_ -> Try.success(42));
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldNotRecoverWithSuccess() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.recoverWith(_ -> Try.success(0))).isEqualTo(Try.success(42));
    }

    // .filter

    @Test
    void shouldFilterSuccess() {
        Try<Integer> actual = Try.success(42).filter(i -> i == 42);
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldNotFilterFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).filter(i -> i == 42);
        assertThat(actual).isEqualTo(Try.failure(exception));
    }

    @Test
    void shouldFilterSuccessPredicateFalse() {
        Try<Integer> actual = Try.success(42).filter(i -> i != 42);
        assertThat(actual).has(new Condition<>(Try::isFailure, "is failure"))
                .has(new Condition<>(t -> t.getFailure() instanceof NoSuchElementException, "is NoSuchElementException"));
    }

    // .filterNot

    @Test
    void shouldFilterNotSuccess() {
        Try<Integer> actual = Try.success(42).filterNot(i -> i == 42);
        assertThat(actual).has(new Condition<>(Try::isFailure, "is failure"))
                .has(new Condition<>(t -> t.getFailure() instanceof NoSuchElementException, "is NoSuchElementException"));
    }

    @Test
    void shouldNotFilterNotFailure() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).filter(i -> i == 42);
        assertThat(actual).isEqualTo(Try.failure(exception));
    }

    @Test
    void shouldFilterSuccessPredicateTrue() {
        Try<Integer> actual = Try.success(42).filterNot(i -> i != 42);
        assertThat(actual).isEqualTo(Try.success(42));
    }

    // .tap

    @Test
    void shouldTapSuccess() {
        StringBuilder sb = new StringBuilder();
        Try<Integer> actual = Try.success(42).tap(sb::append);
        assertThat(sb.toString()).isEqualTo("42");
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldNotTapFailure() {
        StringBuilder sb = new StringBuilder();
        Exception exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).tap(sb::append);
        assertThat(sb.toString()).isEmpty();
        assertThat(actual).isEqualTo(Try.failure(exception));
    }

    // .tapFailure

    @Test
    void shouldNotTapSuccess() {
        StringBuilder sb = new StringBuilder();
        Try<Integer> actual = Try.success(42).tapFailure(sb::append);
        assertThat(sb.toString()).isEmpty();
        assertThat(actual).isEqualTo(Try.success(42));
    }

    @Test
    void shouldTapFailure() {
        StringBuilder sb = new StringBuilder();
        Exception exception = new Exception("Boom");
        Try<Integer> actual = Try.<Integer>failure(exception).tapFailure(e -> sb.append(e.getMessage()));
        assertThat(sb.toString()).isEqualTo("Boom");
        assertThat(actual).isEqualTo(Try.failure(exception));
    }

    // conversion methods

    // .toOption

    @Test
    void shouldConvertSuccessToSome() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.toOption()).isEqualTo(Option.some(42));
    }

    @Test
    void shouldConvertFailureToNone() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.failure(exception);
        assertThat(actual.toOption()).isEqualTo(Option.none());
    }

    // .toEither

    @Test
    void shouldConvertSuccessToRight() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.toEither()).isEqualTo(Either.right(42));
    }

    @Test
    void shouldConvertFailureToLeft() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.failure(exception);
        assertThat(actual.toEither()).isEqualTo(Either.left(exception));
    }

    // .toOptional

    @Test
    void shouldConvertSuccessToOptional() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.toOptional()).isEqualTo(java.util.Optional.of(42));
    }

    @Test
    void shouldConvertFailureToEmptyOptional() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.failure(exception);
        assertThat(actual.toOptional()).isEqualTo(java.util.Optional.empty());
    }

    // .toList

    @Test
    void shouldConvertSuccessToList() {
        Try<Integer> actual = Try.success(42);
        assertThat(actual.toList()).isEqualTo(List.of(42));
    }

    @Test
    void shouldConvertFailureToList() {
        var exception = new Exception("Boom");
        Try<Integer> actual = Try.failure(exception);
        assertThat(actual.toList()).isEqualTo(List.of());
    }
}