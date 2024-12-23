package org.kleinb.jfun;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ValidationTest {

    // factory methods, construction

    @Test
    void shouldCreateValid() {
        Validation<?, Integer> actual = Validation.valid(42);
        assertThat(actual).isInstanceOf(Valid.class);
    }

    @Test
    void shouldCreateInvalid() {
        Validation<String, ?> actual = Validation.invalid("error");
        assertThat(actual).isInstanceOf(Invalid.class);
    }

    @Test
    void shouldCreateFromRight() {
        Validation<String, Integer> actual = Validation.fromEither(Either.right(42));
        assertThat(actual).isInstanceOf(Valid.class);
    }

    @Test
    void shouldCreateFromLeft() {
        Validation<String, Integer> actual = Validation.fromEither(Either.left("error"));
        assertThat(actual).isEqualTo(Validation.invalid("error"));
    }

    @Test
    void shouldCreateFromTrySuccess() {
        Validation<Throwable, Integer> actual = Validation.fromTry(Try.success(42));
        assertThat(actual).isEqualTo(Validation.valid(42));
    }

    @Test
    void shouldCreateFromTryFailure() {
        Exception exception = new RuntimeException("error");
        Validation<Throwable, Integer> actual = Validation.fromTry(Try.failure(exception));
        assertThat(actual).isEqualTo(Validation.invalid(exception));
    }

    // .isValid , .isInvalid
    @Test
    void shouldCheckValid() {
        Validation<?, Integer> valid = Validation.valid(42);
        assertThat(valid.isValid()).isTrue();
        assertThat(valid.isInvalid()).isFalse();
    }

    @Test
    void shouldCheckInvalid() {
        Validation<String, ?> invalid = Validation.invalid("error");
        assertThat(invalid.isValid()).isFalse();
        assertThat(invalid.isInvalid()).isTrue();
    }

    // .get

    @Test
    void shouldGetValidValue() {
        Validation<?, Integer> valid = Validation.valid(42);
        assertThat(valid.get()).isEqualTo(42);
    }

    @Test
    void shouldThrowOnGetInvalid() {
        Validation<String, ?> invalid = Validation.invalid("error");
        assertThatThrownBy(invalid::get).isInstanceOf(NoSuchElementException.class);
    }

    // .getError

    @Test
    void shouldGetInvalidError() {
        Validation<String, ?> invalid = Validation.invalid("error");
        assertThat(invalid.getError()).isEqualTo("error");
    }

    @Test
    void shouldThrowOnGetErrorValid() {
        Validation<?, Integer> valid = Validation.valid(42);
        assertThatThrownBy(valid::getError).isInstanceOf(NoSuchElementException.class);
    }

    // .getOrElse

    @Test
    void shouldGetOrElseValid() {
        Validation<?, Integer> valid = Validation.valid(42);
        assertThat(valid.getOrElse(0)).isEqualTo(42);
    }

    @Test
    void shouldGetOrElseInvalid() {
        Validation<String, String> invalid = Validation.invalid("error");
        assertThat(invalid.getOrElse("42")).isEqualTo("42");
    }

    @Test
    void shouldGetOrElseSupplierValid() {
        Validation<?, Integer> valid = Validation.valid(42);
        assertThat(valid.getOrElse(() -> 0)).isEqualTo(42);
    }

    @Test
    void shouldGetOrElseSupplierInvalid() {
        Validation<String, String> invalid = Validation.invalid("error");
        assertThat(invalid.getOrElse(() -> "42")).isEqualTo("42");
    }

    // .orElse

    @Test
    void shouldReturnValidOnOrElseValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        Validation<String, Integer> or = Validation.invalid("error");
        assertThat(valid.orElse(or)).isEqualTo(valid);
    }

    @Test
    void shouldReturnOrOnOrElseInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        Validation<String, Integer> or = Validation.valid(42);
        assertThat(invalid.orElse(or)).isEqualTo(or);
    }

    @Test
    void shouldReturnValidOnOrElseSupplierValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        Validation<String, Integer> or = Validation.invalid("error");
        assertThat(valid.orElse(() -> or)).isEqualTo(valid);
    }

    @Test
    void shouldReturnOrOnOrElseSupplierInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        Validation<String, Integer> or = Validation.valid(42);
        assertThat(invalid.orElse(() -> or)).isEqualTo(or);
    }

    // .swap

    @Test
    void shouldSwapValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.swap()).isEqualTo(Validation.invalid(42));
    }

    @Test
    void shouldSwapInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        assertThat(invalid.swap()).isEqualTo(Validation.valid("error"));
    }

    // .map

    @Test
    void shouldMapValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.map(String::valueOf)).isEqualTo(Validation.valid("42"));
    }

    @Test
    void shouldMapInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        assertThat(invalid.map(String::valueOf)).isEqualTo(Validation.invalid("error"));
    }

    // .mapError

    @Test
    void shouldMapErrorValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.mapError(String::length)).isEqualTo(Validation.valid(42));
    }

    @Test
    void shouldMapErrorInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        assertThat(invalid.mapError(String::length)).isEqualTo(Validation.invalid(5));
    }

    // .flatMap

    @Test
    void shouldFlatMapValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.flatMap(i -> Validation.valid(String.valueOf(i)))).isEqualTo(Validation.valid("42"));
    }

    @Test
    void shouldFlatMapInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        assertThat(invalid.flatMap(i -> Validation.valid(String.valueOf(i)))).isEqualTo(Validation.invalid("error"));
    }

    @Test
    void shouldFlatMapValidInvalid() {
        Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.flatMap(_ -> Validation.invalid("error"))).isEqualTo(Validation.invalid("error"));
    }

    // .fold
    @Test
    void shouldFoldValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.fold(i -> i, String::length)).isEqualTo(42);
    }

    @Test
    void shouldFoldInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        assertThat(invalid.fold(i -> i, String::length)).isEqualTo(5);
    }

    // .tap

    @Test
    void shouldTapValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        StringBuilder sb = new StringBuilder();
        var actual = valid.tap(sb::append);
        assertThat(sb.toString()).isEqualTo("42");
        assertThat(actual).isEqualTo(valid);
    }

    @Test
    void shouldNotTapInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        StringBuilder sb = new StringBuilder();
        var actual = invalid.tap(sb::append);
        assertThat(sb.toString()).isEmpty();
        assertThat(actual).isEqualTo(invalid);
    }

    // .tapError

    @Test
    void shouldTapErrorValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        StringBuilder sb = new StringBuilder();
        var actual = valid.tapError(sb::append);
        assertThat(sb.toString()).isEmpty();
        assertThat(actual).isEqualTo(valid);
    }

    @Test
    void shouldTapErrorInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        StringBuilder sb = new StringBuilder();
        var actual = invalid.tapError(sb::append);
        assertThat(sb.toString()).isEqualTo("error");
        assertThat(actual).isEqualTo(invalid);
    }

    // .tapBoth

    @Test
    void shouldTapBothValid() {
        Validation<String, Integer> valid = Validation.valid(42);
        StringBuilder sb = new StringBuilder();
        var actual = valid.tapBoth(sb::append, sb::append);
        assertThat(sb.toString()).isEqualTo("42");
        assertThat(actual).isEqualTo(valid);
    }

    @Test
    void shouldTapBothInvalid() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        StringBuilder sb = new StringBuilder();
        var actual = invalid.tapBoth(sb::append, sb::append);
        assertThat(sb.toString()).isEqualTo("error");
        assertThat(actual).isEqualTo(invalid);
    }

    // .toEither

    @Test
    void shouldConvertValidToRight() {
        Validation<String, Integer> valid = Validation.valid(42);
        assertThat(valid.toEither()).isEqualTo(Either.right(42));
    }

    @Test
    void shouldConvertInvalidToLeft() {
        Validation<String, Integer> invalid = Validation.invalid("error");
        assertThat(invalid.toEither()).isEqualTo(Either.left("error"));
    }

}