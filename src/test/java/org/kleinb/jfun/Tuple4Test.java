package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class Tuple4Test {

  @Test
  void append() {
    Tuple4<Integer, String, Boolean, Double> t = Tuple.of(42, "foo", true, 3.14);
    assertThat(t.append(42L)).isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L));
  }

  @Test
  void prepend() {
    Tuple4<Integer, String, Boolean, Double> t = Tuple.of(42, "foo", true, 3.14);
    assertThat(t.prepend(42L)).isEqualTo(Tuple.of(42L, 42, "foo", true, 3.14));
  }

  @Test
  void concat1() {
    Tuple4<Integer, String, Boolean, Double> t = Tuple.of(42, "foo", true, 3.14);
    assertThat(t.concat(Tuple.of(42L))).isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L));
  }

  @Test
  void concat2() {
    Tuple4<Integer, String, Boolean, Double> t = Tuple.of(42, "foo", true, 3.14);
    assertThat(t.concat(Tuple.of(42L, (short) 42)))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42));
  }

  @Test
  void concat3() {
    Tuple4<Integer, String, Boolean, Double> t = Tuple.of(42, "foo", true, 3.14);
    assertThat(t.concat(Tuple.of(42L, (short) 42, List.of(1, 2, 3))))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3)));
  }

  @Test
  void concat4() {
    Tuple4<Integer, String, Boolean, Double> t = Tuple.of(42, "foo", true, 3.14);
    assertThat(t.concat(Tuple.of(42L, (short) 42, List.of(1, 2, 3), "foo")))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3), "foo"));
  }
}
