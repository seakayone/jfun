package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Function6Test {

  @Test
  void constant() {
    Function6<String, String, String, String, String, String, String> f = Function6.constant("foo");
    assertThat(f.apply("bar", "baz", "qux", "quux", "quuz", "quuux")).isEqualTo("foo");
  }

  @Test
  void apply() {
    Function6<String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g) -> a + b + c + d + e + g;
    assertThat(f.apply("foo", "bar", "baz", "qux", "quux", "quuz"))
        .isEqualTo("foobarbazquxquuxquuz");
  }

  @Test
  void curried() {
    Function6<String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g) -> "foo";
    assertThat(
            f.curried()
                .apply("bar")
                .apply("baz")
                .apply("qux")
                .apply("quux")
                .apply("quuz")
                .apply("quuux"))
        .isEqualTo("foo");
  }

  @Test
  void reversed() {
    Function6<String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g) -> a + b + c + d + e + g;
    assertThat(f.reversed().apply("foo", "bar", "baz", "qux", "quux", "quuz"))
        .isEqualTo("quuzquuxquxbazbarfoo");
  }

  @Test
  void tupled() {
    Function6<String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g) -> "foo";
    assertThat(f.tupled().apply(Tuple.of("bar", "baz", "qux", "quux", "quuz", "quuux")))
        .isEqualTo("foo");
  }

  @Test
  void compose() {
    Function6<String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g) -> a + b + c + d + e + g;
    Function1<String, String> h = a -> "h" + a;
    assertThat(f.compose(h).apply("i", "j", "k", "l", "m", "n")).isEqualTo("hijklmn");
  }

  @Test
  void andThen() {
    Function6<String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g) -> a + b + c + d + e + g;
    Function1<String, String> h = a -> "h" + a;
    assertThat(f.andThen(h).apply("i", "j", "k", "l", "m", "n")).isEqualTo("hijklmn");
  }
}
