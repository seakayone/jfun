package org.kleinb.jfun.optics.std;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Either;
import org.kleinb.jfun.optics.PrismLaws;

class EitherOpticsTest {

  @Test
  void testLeftPrismLaws() {
    var leftPrism = EitherOptics.left();
    assertTrue(PrismLaws.partialRoundTripOneWay(leftPrism, Either.left(42)));
    assertTrue(PrismLaws.partialRoundTripOtherWay(leftPrism, 42));
  }

  @Test
  void testRightPrismLaws() {
    var rightPrism = EitherOptics.right();
    assertTrue(PrismLaws.partialRoundTripOneWay(rightPrism, Either.right(42)));
    assertTrue(PrismLaws.partialRoundTripOtherWay(rightPrism, 42));
  }
}
