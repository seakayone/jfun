package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.NonEmptyList;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.optics.Iso;
import org.kleinb.jfun.optics.law.IsoLaws;

class NonEmptyListOpticsTest {

  private final Iso<List<Integer>, Option<NonEmptyList<Integer>>> listToOptionNonEmptyList =
      NonEmptyListOptics.listToOptionNonEmptyList();

  @Test
  void testIsoLaws() {
    assertThat(IsoLaws.roundTripOneWay(listToOptionNonEmptyList, List.of())).isTrue();
    assertThat(IsoLaws.roundTripOneWay(listToOptionNonEmptyList, List.of(1, 2, 3))).isTrue();

    assertThat(IsoLaws.roundTripOtherWay(listToOptionNonEmptyList, Option.none())).isTrue();
    assertThat(
            IsoLaws.roundTripOtherWay(listToOptionNonEmptyList, NonEmptyList.of(List.of(1, 2, 3))))
        .isTrue();
  }
}
