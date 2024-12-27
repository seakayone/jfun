package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Function4Test {

  @Test
  void constant() {
    Function4<String, String, String, String, String> f = Function4.constant("foo");
    assertThat(f.apply("bar", "baz", "qux", "quux")).isEqualTo("foo");
  }

  @Test
  void apply() {
    Function4<String, String, String, String, String> f = (a, b, c, d) -> a + b + c + d;
    assertThat(f.apply("foo", "bar", "baz", "qux")).isEqualTo("foobarbazqux");
  }

  @Test
  void applyA() {
    Function4<String, String, String, String, String> f = (a, b, c, d) -> a + b + c + d;
    Function3<String, String, String, String> g = f.apply("foo");
    assertThat(g.apply("bar", "baz", "qux")).isEqualTo("foobarbazqux");
  }

  @Test
  void curried() {
    Function4<String, String, String, String, String> f = (a, b, c, d) -> "foo";
    assertThat(f.curried().apply("bar").apply("baz").apply("qux").apply("quux")).isEqualTo("foo");
  }

  @Test
  void reversed() {
    Function4<String, String, String, String, String> f = (a, b, c, d) -> a + b + c + d;
    assertThat(f.reversed().apply("foo", "bar", "baz", "qux")).isEqualTo("quxbazbarfoo");
  }

  @Test
  void tupled() {
    Function4<String, String, String, String, String> f = (a, b, c, d) -> "foo";
    assertThat(f.tupled().apply(Tuple.of("bar", "baz", "qux", "quux"))).isEqualTo("foo");
  }

  @Test
  void compose() {
    Function4<String, String, String, String, String> f = (a, b, c, d) -> "f" + a + b + c + d;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.compose(g).apply("h", "i", "j", "k")).isEqualTo("fghijk");
  }

  @Test
  void andThen() {
    Function4<String, String, String, String, String> f = (a, b, c, d) -> a + b + c + d;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.andThen(g).apply("h", "i", "j", "k")).isEqualTo("ghijk");
  }
}
