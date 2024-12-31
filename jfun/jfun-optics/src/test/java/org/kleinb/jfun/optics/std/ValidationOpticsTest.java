package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Validation;
import org.kleinb.jfun.optics.Prism;
import org.kleinb.jfun.optics.law.PrismLaws;

class ValidationOpticsTest {
  @Test
  void testValidPrismLaws() {
    final Prism<Validation<Throwable, Integer>, Integer> validPrism = ValidationOptics.valid();
    assertThat(PrismLaws.partialRoundTripOneWay(validPrism, Validation.valid(42))).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(validPrism, 42)).isTrue();
  }

  @Test
  void testInvalidPrismLaws() {
    final Prism<Validation<String, Integer>, List<String>> invalidPrism =
        ValidationOptics.invalid();
    assertThat(PrismLaws.partialRoundTripOneWay(invalidPrism, Validation.invalid("error")))
        .isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(invalidPrism, List.of("error"))).isTrue();
  }
}
