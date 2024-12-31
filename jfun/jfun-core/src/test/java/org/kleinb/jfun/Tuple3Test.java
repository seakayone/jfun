package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class Tuple3Test {

  @Test
  void append() {
    Tuple3<Integer, String, Boolean> t = Tuple.of(42, "foo", true);
    assertThat(t.append(3.14)).isEqualTo(Tuple.of(42, "foo", true, 3.14));
  }

  @Test
  void prepend() {
    Tuple3<Integer, String, Boolean> t = Tuple.of(42, "foo", true);
    assertThat(t.prepend(3.14)).isEqualTo(Tuple.of(3.14, 42, "foo", true));
  }

  @Test
  void concat1() {
    Tuple3<Integer, String, Boolean> t = Tuple.of(42, "foo", true);
    assertThat(t.concat(Tuple.of(3.14))).isEqualTo(Tuple.of(42, "foo", true, 3.14));
  }

  @Test
  void concat2() {
    Tuple3<Integer, String, Boolean> t = Tuple.of(42, "foo", true);
    assertThat(t.concat(Tuple.of(3.14, 42L))).isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L));
  }

  @Test
  void concat3() {
    Tuple3<Integer, String, Boolean> t = Tuple.of(42, "foo", true);
    assertThat(t.concat(Tuple.of(3.14, 42L, (short) 42)))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42));
  }

  @Test
  void concat4() {
    Tuple3<Integer, String, Boolean> t = Tuple.of(42, "foo", true);
    assertThat(t.concat(Tuple.of(3.14, 42L, (short) 42, List.of(1, 2, 3))))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3)));
  }

  @Test
  void concat5() {
    Tuple3<Integer, String, Boolean> t = Tuple.of(42, "foo", true);
    assertThat(t.concat(Tuple.of(3.14, 42L, (short) 42, List.of(1, 2, 3), Map.of("foo", 42))))
        .isEqualTo(
            Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3), Map.of("foo", 42)));
  }
}
