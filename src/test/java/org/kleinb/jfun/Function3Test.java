package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Function3Test {

  @Test
  void constant() {
    Function3<String, String, String, String> f = Function3.constant("foo");
    assertThat(f.apply("bar", "baz", "qux")).isEqualTo("foo");
  }

  @Test
  void apply() {
    Function3<String, String, String, String> f = (a, b, c) -> a + b + c;
    assertThat(f.apply("foo", "bar", "baz")).isEqualTo("foobarbaz");
  }

  @Test
  void curried() {
    Function3<String, String, String, String> f = (a, b, c) -> "foo";
    assertThat(f.curried().apply("bar").apply("baz").apply("qux")).isEqualTo("foo");
  }

  @Test
  void tupled() {
    Function3<String, String, String, String> f = (a, b, c) -> "foo";
    assertThat(f.tupled().apply(Tuple.of("bar", "baz", "qux"))).isEqualTo("foo");
  }

  @Test
  void compose() {
    Function3<String, String, String, String> f = (a, b, c) -> "f" + a + b + c;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.compose(g).apply("h", "i", "j")).isEqualTo("fghij");
  }

  @Test
  void andThen() {
    Function3<String, String, String, String> f = (a, b, c) -> a + b + c;
    Function1<String, String> g = a -> "g" + a;
    assertThat(f.andThen(g).apply("h", "i", "j")).isEqualTo("ghij");
  }
}
