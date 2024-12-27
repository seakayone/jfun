package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Tuple0Test {

  @Test
  void append() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.append("foo")).isEqualTo(Tuple.of("foo"));
  }

  @Test
  void prepend() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.prepend("foo")).isEqualTo(Tuple.of("foo"));
  }

  @Test
  void concat() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo"))).isEqualTo(Tuple.of("foo"));
  }

  @Test
  void concat2() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo", 42))).isEqualTo(Tuple.of("foo", 42));
  }

  @Test
  void concat3() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo", 42, true))).isEqualTo(Tuple.of("foo", 42, true));
  }

  @Test
  void concat4() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14)))
        .isEqualTo(Tuple.of("foo", 42, true, 3.14));
  }

  @Test
  void concat5() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14, 42L)))
        .isEqualTo(Tuple.of("foo", 42, true, 3.14, 42L));
  }

  @Test
  void concat6() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42)))
        .isEqualTo(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42));
  }

  @Test
  void concat7() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42, 42.0f)))
        .isEqualTo(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42, 42.0f));
  }

  @Test
  void concat8() {
    Tuple0<Integer> t = Tuple.empty();
    assertThat(t.concat(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42, 42.0f, 42.0)))
        .isEqualTo(Tuple.of("foo", 42, true, 3.14, 42L, (short) 42, 42.0f, 42.0));
  }
}
