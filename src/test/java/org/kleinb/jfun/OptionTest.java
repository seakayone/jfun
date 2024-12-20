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
}