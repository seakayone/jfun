package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Function2Test {

  @Test
  void constant() {
    Function2<String, String, String> f = Function2.constant("foo");
    assertThat(f.apply("bar", "baz")).isEqualTo("foo");
  }

  @Test
  void apply() {
    Function2<String, String, String> f = (a, b) -> a + b;
    assertThat(f.apply("foo", "bar")).isEqualTo("foobar");
  }

  @Test
  void applyA() {
    Function2<String, String, String> f = (a, b) -> a + b;
    Function1<String, String> g = f.apply("foo");
    assertThat(g.apply("bar")).isEqualTo("foobar");
  }

  @Test
  void curried() {
    Function2<String, String, String> f = (a, b) -> "foo";
    assertThat(f.curried().apply("bar").apply("baz")).isEqualTo("foo");
  }

  @Test
  void tupled() {
    Function2<String, String, String> f = (a, b) -> "foo";
    assertThat(f.tupled().apply(Tuple.of("bar", "baz"))).isEqualTo("foo");
  }

  @Test
  void compose() {
    Function2<String, String, String> f = (a, b) -> "f" + a + b;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.compose(g).apply("h", "i")).isEqualTo("fghi");
  }

  @Test
  void andThen() {
    Function2<String, String, String> f = (a, b) -> a + b;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.andThen(g).apply("h", "i")).isEqualTo("ghi");
  }

  @Test
  void reversed() {
    Function2<String, String, String> f = (a, b) -> a + b;
    assertThat(f.reversed().apply("foo", "bar")).isEqualTo("barfoo");
  }
}
