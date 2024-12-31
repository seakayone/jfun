package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.optics.Iso;
import org.kleinb.jfun.optics.Prism;
import org.kleinb.jfun.optics.law.IsoLaws;
import org.kleinb.jfun.optics.law.PrismLaws;

class OptionOpticsTest {

  @Test
  void somePrismLaws() {
    final Prism<Option<Integer>, Integer> somePrism = OptionOptics.some();
    assertThat(PrismLaws.partialRoundTripOneWay(somePrism, Option.some(42))).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(somePrism, 42)).isTrue();
  }

  @Test
  void nonePrismLaws() {
    final Prism<Option<Integer>, Void> nonePrism = OptionOptics.none();
    assertThat(PrismLaws.partialRoundTripOneWay(nonePrism, Option.none())).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(nonePrism, null)).isTrue();
  }

  @Test
  void withDefaultIsoLaws() {
    final Iso<Option<Integer>, Integer> withDefault = OptionOptics.withDefault(42);
    assertThat(IsoLaws.roundTripOneWay(withDefault, Option.none())).isTrue();
    assertThat(IsoLaws.roundTripOneWay(withDefault, Option.some(0))).isTrue();
    assertThat(IsoLaws.roundTripOtherWay(withDefault, 0)).isTrue();
    assertThat(IsoLaws.roundTripOtherWay(withDefault, 42)).isTrue();
  }
}
