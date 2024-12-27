package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class Tuple2Test {

  @Test
  void append() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.append(true)).isEqualTo(Tuple.of(42, "foo", true));
  }

  @Test
  void prepend() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.prepend(true)).isEqualTo(Tuple.of(true, 42, "foo"));
  }

  @Test
  void concat1() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.concat(Tuple.of(true))).isEqualTo(Tuple.of(42, "foo", true));
  }

  @Test
  void concat2() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.concat(Tuple.of(true, 3.14))).isEqualTo(Tuple.of(42, "foo", true, 3.14));
  }

  @Test
  void concat3() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.concat(Tuple.of(true, 3.14, 42L))).isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L));
  }

  @Test
  void concat4() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.concat(Tuple.of(true, 3.14, 42L, (short) 42)))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42));
  }

  @Test
  void concat5() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.concat(Tuple.of(true, 3.14, 42L, (short) 42, List.of(1, 2, 3))))
        .isEqualTo(Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3)));
  }

  @Test
  void concat6() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.concat(Tuple.of(true, 3.14, 42L, (short) 42, List.of(1, 2, 3), Map.of("foo", 42))))
        .isEqualTo(
            Tuple.of(42, "foo", true, 3.14, 42L, (short) 42, List.of(1, 2, 3), Map.of("foo", 42)));
  }

  @Test
  void toEntry() {
    Tuple2<Integer, String> t = Tuple.of(42, "foo");
    assertThat(t.toEntry()).isEqualTo(Map.entry(42, "foo"));
  }
}
