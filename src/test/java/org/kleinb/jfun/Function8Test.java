package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Function8Test {

  @Test
  void constant() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        Function8.constant("foo");
    assertThat(f.apply("bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux", "quuuuux"))
        .isEqualTo("foo");
  }

  @Test
  void apply() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h, i) -> a + b + c + d + e + g + h + i;
    assertThat(f.apply("foo", "bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux"))
        .isEqualTo("foobarbazquxquuxquuzquuuxquuuux");
  }

  @Test
  void applyA() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h, i) -> a + b + c + d + e + g + h + i;
    Function7<String, String, String, String, String, String, String, String> g = f.apply("foo");
    assertThat(g.apply("bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux"))
        .isEqualTo("foobarbazquxquuxquuzquuuxquuuux");
  }

  @Test
  void curried() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h, i) -> "foo";
    assertThat(
            f.curried()
                .apply("bar")
                .apply("baz")
                .apply("qux")
                .apply("quux")
                .apply("quuz")
                .apply("quuux")
                .apply("quuuux")
                .apply("quuuuux"))
        .isEqualTo("foo");
  }

  @Test
  void reversed() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h, i) -> a + b + c + d + e + g + h + i;
    assertThat(f.reversed().apply("foo", "bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux"))
        .isEqualTo("quuuuxquuuxquuzquuxquxbazbarfoo");
  }

  @Test
  void tupled() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h, i) -> a + b + c + d + e + g + h + i;
    assertThat(
            f.tupled()
                .apply(Tuple.of("bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux", "quuuuux")))
        .isEqualTo("barbazquxquuxquuzquuuxquuuuxquuuuux");
  }

  @Test
  void compose() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h, i) -> a + b + c + d + e + g + h + i;
    Function1<String, String> before = s -> s + "foo";
    assertThat(
            f.compose(before)
                .apply("bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux", "quuuuux"))
        .isEqualTo("barfoobazquxquuxquuzquuuxquuuuxquuuuux");
  }

  @Test
  void andThen() {
    Function8<String, String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h, i) -> a + b + c + d + e + g + h + i;
    Function1<String, String> after = s -> s + "foo";
    assertThat(
            f.andThen(after)
                .apply("bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux", "quuuuux"))
        .isEqualTo("barbazquxquuxquuzquuuxquuuuxquuuuuxfoo");
  }
}
