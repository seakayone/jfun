package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Tuple1Test {

  @Test
  void append() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.append("foo")).isEqualTo(Tuple.of(42, "foo"));
  }

  @Test
  void prepend() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.prepend("foo")).isEqualTo(Tuple.of("foo", 42));
  }

  @Test
  void concat1() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.concat(Tuple.of("foo"))).isEqualTo(Tuple.of(42, "foo"));
  }

  @Test
  void concat2() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.concat(Tuple.of("foo", 42))).isEqualTo(Tuple.of(42, "foo", 42));
  }

  @Test
  void concat3() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.concat(Tuple.of("foo", 42, true))).isEqualTo(Tuple.of(42, "foo", 42, true));
  }

  @Test
  void concat4() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14)))
        .isEqualTo(Tuple.of(42, "foo", 42, true, 3.14));
  }

  @Test
  void concat5() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14, 42L)))
        .isEqualTo(Tuple.of(42, "foo", 42, true, 3.14, 42L));
  }

  @Test
  void concat6() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42)))
        .isEqualTo(Tuple.of(42, "foo", 42, true, 3.14, 42L, (short) 42));
  }

  @Test
  void concat7() {
    Tuple1<Integer> t = Tuple.of(42);
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42, 42.0f)))
        .isEqualTo(Tuple.of(42, "foo", 42, true, 3.14, 42L, (short) 42, 42.0f));
  }
}
