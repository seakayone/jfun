package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.Iso;
import org.kleinb.jfun.optics.law.IsoLaws;

class MapOpticsTest {

  @Test
  void testIsoMapToSet() {
    final Iso<Map<Integer, Void>, Set<Integer>> iso = MapOptics.mapToSet();
    final Map<Integer, Void> map =
        Collections.unmodifiableMap(
            new HashMap<>() {
              {
                put(1, null);
                put(2, null);
                put(3, null);
              }
            });

    assertThat(IsoLaws.roundTripOtherWay(iso, Set.of(1, 2, 3))).isTrue();
    assertThat(IsoLaws.roundTripOtherWay(iso, Set.of())).isTrue();

    assertThat(IsoLaws.roundTripOneWay(iso, map)).isTrue();
    assertThat(IsoLaws.roundTripOneWay(iso, Map.of())).isTrue();
  }
}
