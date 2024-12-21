package org.kleinb.jfun;


import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OptionTest {

    @Test
    void some() {
        Option<Integer> option = Option.some(42);
        assertThat(option.isSome()).isTrue();
        assertThat(option.isNone()).isFalse();
        assertThat(option.get()).isEqualTo(42);
        assertThat(option.contains(42)).isTrue();
        assertThat(option.exists(a -> a.equals(42))).isTrue();
    }

    @Test
    void none() {
        Option<Integer> option = Option.none();
        assertThat(option.isSome()).isFalse();
        assertThat(option.isNone()).isTrue();
        assertThatThrownBy(option::get).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void mapWithSome() {
        Option<Integer> option = Option.some(42);
        Option<String> mapped = option.map(Object::toString);
        assertThat(mapped.isSome()).isTrue();
        assertThat(mapped.get()).isEqualTo("42");
    }

    @Test
    void mapWithNone() {
        Option<Integer> option = Option.none();
        Option<String> mapped = option.map(Object::toString);
        assertThat(mapped.isNone()).isTrue();
    }

    @Test
    void flatMapWithSome() {
        Option<Integer> option = Option.some(42);
        Option<String> mapped = option.flatMap(i -> Option.some(i.toString()));
        assertThat(mapped.isSome()).isTrue();
        assertThat(mapped.get()).isEqualTo("42");
    }

    @Test
    void flatMapWithNone() {
        Option<Integer> option = Option.none();
        Option<String> mapped = option.flatMap(i -> Option.some(i.toString()));
        assertThat(mapped.isNone()).isTrue();
    }

    @Test
    void flatMapWithSomeAndNone() {
        Option<Integer> option = Option.some(42);
        Option<String> mapped = option.flatMap(i -> Option.none());
        assertThat(mapped.isNone()).isTrue();
    }

    @Test
    void foldWithSome() {
        Option<Integer> option = Option.some(42);
        String result = option.fold(Object::toString, () -> "none");
        assertThat(result).isEqualTo("42");
    }

    @Test
    void foldWithNone() {
        Option<Integer> option = Option.none();
        String result = option.fold(Object::toString, () -> "none");
        assertThat(result).isEqualTo("none");
    }

    @Test
    void orElseWithSome() {
        Option<Integer> option = Option.some(42);
        Option<Integer> result = option.orElse(() -> Option.some(0));
        assertThat(result.isSome()).isTrue();
        assertThat(result.get()).isEqualTo(42);
    }

    @Test
    void orElseWithNone() {
        Option<Integer> option = Option.none();
        Option<Integer> result = option.orElse(() -> Option.some(0));
        assertThat(result.isSome()).isTrue();
        assertThat(result.get()).isEqualTo(0);
    }

    @Test
    void orElseWithNoneAndNone() {
        Option<Integer> option = Option.none();
        Option<Integer> result = option.orElse(() -> Option.none());
        assertThat(result.isNone()).isTrue();
    }
}