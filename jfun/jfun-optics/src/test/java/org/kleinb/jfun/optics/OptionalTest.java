package org.kleinb.jfun.optics;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Either;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.optics.law.OptionalLaws;

class OptionalTest {

  // zooming into the head of a list
  private static final Optional<List<Integer>, Integer> head =
      Optional.of(
          list -> Option.ofOptional(list.stream().findFirst()),
          (i, list) -> {
            if (list.isEmpty()) {
              return list;
            } else {
              var head = Stream.of(i);
              var tail = list.stream().skip(1);
              return Stream.concat(head, tail).toList();
            }
          });

  private static final Optional<Integer, String> intToString =
      Optional.of(
          i -> Option.some(i.toString()),
          (s, i) -> {
            try {
              return Integer.parseInt(s);
            } catch (NumberFormatException e) {
              return i;
            }
          });

  private static final List<Integer> list = List.of(1, 2, 3);
  private static final List<Integer> emptyList = List.of();

  // .nonEmpty

  @Test
  void testNonEmptyIsEmpty() {
    assertThat(head.nonEmpty(list)).isTrue();
    assertThat(head.isEmpty(list)).isFalse();
    assertThat(head.nonEmpty(emptyList)).isFalse();
    assertThat(head.isEmpty(emptyList)).isTrue();
  }

  // .getOrModify

  @Test
  void testGetOrModify() {
    assertThat(head.getOrModify(list)).isEqualTo(Either.right(list.getFirst()));
    assertThat(head.getOrModify(emptyList)).isEqualTo(Either.left(emptyList));
  }

  // .getOption

  @Test
  void testGetOption() {
    assertThat(head.getOption(list)).isEqualTo(Option.some(list.getFirst()));
    assertThat(head.getOption(emptyList)).isEqualTo(Option.none());
  }

  // .replace

  @Test
  void testReplace() {
    assertThat(head.replace(42).apply(list)).containsExactly(42, 2, 3);
    assertThat(head.replace(42).apply(emptyList)).isEmpty();
  }

  // .replaceOption

  @Test
  void testReplaceOption() {
    assertThat(head.replaceOption(42).apply(list)).isEqualTo(Option.some(List.of(42, 2, 3)));
    assertThat(head.replaceOption(42).apply(emptyList)).isEqualTo(Option.none());
  }

  // .modify

  @Test
  void testModify() {
    assertThat(head.modify(i -> i + 1).apply(list)).containsExactly(2, 2, 3);
    assertThat(head.modify(i -> i + 1).apply(emptyList)).isEmpty();
  }

  // .modifyOption

  @Test
  void testModifyOption() {
    assertThat(head.modifyOption(i -> i + 1).apply(list)).isEqualTo(Option.some(List.of(2, 2, 3)));
    assertThat(head.modifyOption(i -> i + 1).apply(emptyList)).isEqualTo(Option.none());
  }

  // OptionalLaws

  @Test
  void testOptionalLaws() {
    assertThat(OptionalLaws.getOptionSet(list, head)).isTrue();
    assertThat(OptionalLaws.getOptionSet(emptyList, head)).isTrue();

    assertThat(OptionalLaws.setGetOption(list, 42, head)).isTrue();
    assertThat(OptionalLaws.setGetOption(emptyList, 42, head)).isTrue();
  }

  // .andThen

  @Test
  void testAndThenOptionalLaws() {
    var composed = head.andThen(intToString);
    assertThat(OptionalLaws.getOptionSet(list, composed)).isTrue();
    assertThat(OptionalLaws.getOptionSet(emptyList, composed)).isTrue();

    assertThat(OptionalLaws.setGetOption(list, "42", composed)).isTrue();
    assertThat(OptionalLaws.setGetOption(emptyList, "42", composed)).isTrue();
  }
}
