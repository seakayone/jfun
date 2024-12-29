package org.kleinb.jfun.optics;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.optics.law.PrismLaws;
import org.kleinb.jfun.optics.std.DoubleOptics;

class PrismTest {

  sealed interface Json permits Json.JNum, Json.JStr, Json.JNull, Json.JObj {
    record JNull() implements Json {}

    record JStr(String v) implements Json {}

    record JNum(double v) implements Json {}

    record JObj(Map<String, Json> v) implements Json {}

    static Prism<Json, String> jStrPrism() {
      return Prism.of(PartialFunction.caseOf(JStr.class, JStr::v), JStr::new);
    }

    static Prism<Json, Double> jNumPrism() {
      return Prism.of(PartialFunction.caseOf(JNum.class, JNum::v), JNum::new);
    }
  }

  // .getOption

  @Test
  void testPrismMatches() {
    Json json = new Json.JStr("hello");
    assertThat(Json.jStrPrism().getOption(json)).isEqualTo(Option.some("hello"));
  }

  @Test
  void testPrismDoesNotMatch() {
    Json json = new Json.JNum(42);
    assertThat(Json.jStrPrism().getOption(json)).isEqualTo(Option.none());
  }

  // .replace

  @Test
  void testPrismReplace() {
    Json json = new Json.JStr("hello");
    assertThat(Json.jStrPrism().replace("world").apply(json)).isEqualTo(new Json.JStr("world"));
  }

  @Test
  void testPrismReplaceDoesNotMatch() {
    Json json = new Json.JNum(42);
    assertThat(Json.jStrPrism().replace("world").apply(json)).isEqualTo(json);
  }

  // .modify

  @Test
  void testPrismModify() {
    Json json = new Json.JStr("hello");
    Prism<Json, String> jsonStringPrism = Json.jStrPrism();
    assertThat(jsonStringPrism.modify(String::toUpperCase).apply(json))
        .isEqualTo(new Json.JStr("HELLO"));
  }

  @Test
  void testPrismModifyDoesNotMatch() {
    Json json = new Json.JNum(42);
    assertThat(Json.jStrPrism().modify(String::toUpperCase).apply(json)).isEqualTo(json);
  }

  @Test
  void testPrismLaws() {
    assertThat(PrismLaws.partialRoundTripOneWay(Json.jStrPrism(), new Json.JStr("hello"))).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(Json.jStrPrism(), "hello")).isTrue();
  }

  // .modifyOption

  @Test
  void testPrismModifyOption() {
    Json json = new Json.JStr("hello");
    assertThat(Json.jStrPrism().modifyOption(String::toUpperCase).apply(json))
        .isEqualTo(Option.some(new Json.JStr("HELLO")));
  }

  @Test
  void testPrismModifyOptionDoesNotMatch() {
    Json json = new Json.JNum(42);
    assertThat(Json.jStrPrism().modifyOption(String::toUpperCase).apply(json))
        .isEqualTo(Option.none());
  }

  // .replaceOption

  @Test
  void testPrismReplaceOption() {
    Json json = new Json.JStr("hello");
    assertThat(Json.jStrPrism().replaceOption("world").apply(json))
        .isEqualTo(Option.some(new Json.JStr("world")));
  }

  @Test
  void testPrismReplaceOptionDoesNotMatch() {
    Json json = new Json.JNum(42);
    assertThat(Json.jStrPrism().replaceOption("world").apply(json)).isEqualTo(Option.none());
  }

  // .andThen

  @Test
  void testPrismAndThen() {
    final Prism<Json, Integer> foo = Json.jNumPrism().andThen(DoubleOptics.doubleToInteger());
    Json json = new Json.JNum(42.0);
    assertThat(foo.getOption(json)).isEqualTo(Option.some(42));
    assertThat(foo.replace(5).apply(json)).isEqualTo(new Json.JNum(5));
    assertThat(foo.modify(i -> i + 1).apply(json)).isEqualTo(new Json.JNum(43));
    assertThat(foo.reverseGet(42)).isEqualTo(json);
  }
}
