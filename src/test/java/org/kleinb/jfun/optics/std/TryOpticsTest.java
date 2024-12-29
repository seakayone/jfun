package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Prism;
import org.kleinb.jfun.optics.PrismLaws;

class TryOpticsTest {
  @Test
  void testSuccessPrismLaws() {
    final Prism<Try<Integer>, Integer> successPrism = TryOptics.success();
    assertThat(PrismLaws.partialRoundTripOneWay(successPrism, Try.success(42))).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(successPrism, 42)).isTrue();
  }

  @Test
  void testFailurePrismLaws() {
    final Prism<Try<Object>, Throwable> failurePrism = TryOptics.failure();
    final RuntimeException exception = new RuntimeException();
    assertThat(PrismLaws.partialRoundTripOneWay(failurePrism, Try.failure(exception))).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(failurePrism, exception)).isTrue();
  }
}
