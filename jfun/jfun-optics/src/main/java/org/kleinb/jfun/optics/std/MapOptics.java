package org.kleinb.jfun.optics.std;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.kleinb.jfun.optics.Iso;

public final class MapOptics {
  private MapOptics() {}

  public static <K> Iso<Map<K, Void>, Set<K>> mapToSet() {
    return Iso.of(
        Map::keySet,
        set -> {
          final Map<K, Void> map = new HashMap<>();
          set.forEach(k -> map.put(k, null));
          return Collections.unmodifiableMap(map);
        });
  }
}
