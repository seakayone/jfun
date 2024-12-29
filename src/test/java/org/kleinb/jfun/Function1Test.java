package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Function1Test {

  @Test
  void constant() {
    Function1<String, String> f = Function1.constant("foo");
    assertThat(f.apply("bar")).isEqualTo("foo");
  }

  @Test
  void identity() {
    Function1<String, String> f = Function1.identity();
    assertThat(f.apply("foo")).isEqualTo("foo");
  }

  @Test
  void apply() {
    Function1<String, String> f = a -> "foo";
    assertThat(f.apply("bar")).isEqualTo("foo");
  }

  @Test
  void methodReference() {
    Function1<String, String> f = Function1.of(String::toUpperCase);
    assertThat(f.apply("bar")).isEqualTo("BAR");
  }

  @Test
  void reversed() {
    Function1<String, String> f = a -> "foo";
    assertThat(f.reversed().apply("bar")).isEqualTo("foo");
  }

  @Test
  void curried() {
    Function1<String, String> f = a -> "foo";
    assertThat(f.curried().apply("bar")).isEqualTo("foo");
  }

  @Test
  void tupled() {
    Function1<String, String> f = a -> "foo";
    assertThat(f.tupled().apply(Tuple.of("bar"))).isEqualTo("foo");
  }

  @Test
  void compose() {
    Function1<String, String> f = a -> "f" + a;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.compose(g).apply("h")).isEqualTo("fgh");
  }

  @Test
  void andThen() {
    Function1<String, String> f = a -> "f" + a;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.andThen(g).apply("h")).isEqualTo("gfh");
  }
}
