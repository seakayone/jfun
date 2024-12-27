package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class Tuple7Test {

  @Test
  void append() {
    Tuple7<Integer, String, Boolean, Double, Long, Short, Integer> t =
        Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, 42);
    assertThat(t.append(List.of(1, 2, 3)))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, 42, List.of(1, 2, 3)));
  }

  @Test
  void prepend() {
    Tuple7<Integer, String, Boolean, Double, Long, Short, Integer> t =
        Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, 42);
    assertThat(t.prepend(List.of(1, 2, 3)))
        .isEqualTo(Tuple.of(List.of(1, 2, 3), 42, "foo", true, 3.14, 42L, (short) 42, 42));
  }

  @Test
  void concat1() {
    Tuple7<Integer, String, Boolean, Double, Long, Short, Integer> t =
        Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, 42);
    assertThat(t.concat(Tuple.of(List.of(1, 2, 3))))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, 42, List.of(1, 2, 3)));
  }
}
