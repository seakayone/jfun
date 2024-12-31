package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class Tuple5Test {

  @Test
  void append() {
    Tuple5<Integer, String, Boolean, Double, Long> t = Tuple.of(42, "foo", true, 3.14, 42L);
    assertThat(t.append((short) 42)).isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42));
  }

  @Test
  void prepend() {
    Tuple5<Integer, String, Boolean, Double, Long> t = Tuple.of(42, "foo", true, 3.14, 42L);
    assertThat(t.prepend((short) 42)).isEqualTo(Tuple.of((short) 42, 42, "foo", true, 3.14, 42L));
  }

  @Test
  void concat1() {
    Tuple5<Integer, String, Boolean, Double, Long> t = Tuple.of(42, "foo", true, 3.14, 42L);
    assertThat(t.concat(Tuple.of((short) 42)))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42));
  }

  @Test
  void concat2() {
    Tuple5<Integer, String, Boolean, Double, Long> t = Tuple.of(42, "foo", true, 3.14, 42L);
    assertThat(t.concat(Tuple.of((short) 42, List.of(1, 2, 3))))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3)));
  }

  @Test
  void concat3() {
    Tuple5<Integer, String, Boolean, Double, Long> t = Tuple.of(42, "foo", true, 3.14, 42L);
    assertThat(t.concat(Tuple.of((short) 42, List.of(1, 2, 3), "foo")))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3), "foo"));
  }
}
