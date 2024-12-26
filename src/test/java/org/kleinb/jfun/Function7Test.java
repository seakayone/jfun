package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Function7Test {

  @Test
  void constant() {
    Function7<String, String, String, String, String, String, String, String> f =
        Function7.constant("foo");
    assertThat(f.apply("bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux")).isEqualTo("foo");
  }

  @Test
  void apply() {
    Function7<String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h) -> a + b + c + d + e + g + h;
    assertThat(f.apply("foo", "bar", "baz", "qux", "quux", "quuz", "quuux"))
        .isEqualTo("foobarbazquxquuxquuzquuux");
  }

  @Test
  void curried() {
    Function7<String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h) -> "foo";
    assertThat(
            f.curried()
                .apply("bar")
                .apply("baz")
                .apply("qux")
                .apply("quux")
                .apply("quuz")
                .apply("quuux")
                .apply("quuuux"))
        .isEqualTo("foo");
  }

  @Test
  void reversed() {
    Function7<String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h) -> a + b + c + d + e + g + h;
    assertThat(f.reversed().apply("foo", "bar", "baz", "qux", "quux", "quuz", "quuux"))
        .isEqualTo("quuuxquuzquuxquxbazbarfoo");
  }

  @Test
  void tupled() {
    Function7<String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h) -> a + b + c + d + e + g + h;
    assertThat(f.tupled().apply(Tuple.of("bar", "baz", "qux", "quux", "quuz", "quuux", "quuuux")))
        .isEqualTo("barbazquxquuxquuzquuuxquuuux");
  }

  @Test
  void compose() {
    Function7<String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h) -> "f" + a + b + c + d + e + g + h;
    Function1<String, String> i = a -> "i" + a;
    assertThat(f.compose(i).apply("j", "k", "l", "m", "n", "o", "p")).isEqualTo("fijklmnop");
  }

  @Test
  void andThen() {
    Function7<String, String, String, String, String, String, String, String> f =
        (a, b, c, d, e, g, h) -> a + b + c + d + e + g + h;
    Function1<String, String> i = a -> "i" + a;
    assertThat(f.andThen(i).apply("j", "k", "l", "m", "n", "o", "p")).isEqualTo("ijklmnop");
  }
}
