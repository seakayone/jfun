package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Function0Test {

  @Test
  void constant() {
    Function0<String> f = Function0.constant("foo");
    assertThat(f.apply()).isEqualTo("foo");
  }

  @Test
  void curried() {
    Function0<String> f = () -> "foo";
    assertThat(f.curried().apply()).isEqualTo("foo");
  }

  @Test
  void tupled() {
    Function0<String> f = () -> "foo";
    assertThat(f.tupled().apply(Tuple.empty())).isEqualTo("foo");
  }

  @Test
  void andThen() {
    Function0<String> f = () -> "foo";
    Function0<Integer> g = f.andThen(String::length);
    assertThat(g.apply()).isEqualTo(3);
  }
}
