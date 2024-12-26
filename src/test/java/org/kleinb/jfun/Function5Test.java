package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Function5Test {

  @Test
  void constant() {
    Function5<String, String, String, String, String, String> f = Function5.constant("foo");
    assertThat(f.apply("bar", "baz", "qux", "quux", "quuz")).isEqualTo("foo");
  }

  @Test
  void apply() {
    Function5<String, String, String, String, String, String> f =
        (a, b, c, d, e) -> a + b + c + d + e;
    assertThat(f.apply("foo", "bar", "baz", "qux", "quux")).isEqualTo("foobarbazquxquux");
  }

  @Test
  void curried() {
    Function5<String, String, String, String, String, String> f = (a, b, c, d, e) -> "foo";
    assertThat(f.curried().apply("bar").apply("baz").apply("qux").apply("quux").apply("quuz"))
        .isEqualTo("foo");
  }

  @Test
  void reversed() {
    Function5<String, String, String, String, String, String> f =
        (a, b, c, d, e) -> a + b + c + d + e;
    assertThat(f.reversed().apply("foo", "bar", "baz", "qux", "quux"))
        .isEqualTo("quuxquxbazbarfoo");
  }

  @Test
  void tupled() {
    Function5<String, String, String, String, String, String> f = (a, b, c, d, e) -> "foo";
    assertThat(f.tupled().apply(Tuple.of("bar", "baz", "qux", "quux", "quuz"))).isEqualTo("foo");
  }

  @Test
  void compose() {
    Function5<String, String, String, String, String, String> f =
        (a, b, c, d, e) -> "f" + a + b + c + d + e;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.compose(g).apply("h", "i", "j", "k", "l")).isEqualTo("fghijkl");
  }

  @Test
  void andThen() {
    Function5<String, String, String, String, String, String> f =
        (a, b, c, d, e) -> a + b + c + d + e;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.andThen(g).apply("h", "i", "j", "k", "l")).isEqualTo("ghijkl");
  }
}
