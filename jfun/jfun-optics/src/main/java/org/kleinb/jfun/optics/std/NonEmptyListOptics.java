package org.kleinb.jfun.optics.std;

import java.util.List;
import org.kleinb.jfun.NonEmptyList;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.optics.Iso;

public final class NonEmptyListOptics {
  private NonEmptyListOptics() {}

  public static <A> Iso<List<A>, Option<NonEmptyList<A>>> listToOptionNonEmptyList() {
    return Iso.of(
        (List<A> list) -> list.isEmpty() ? Option.none() : NonEmptyList.of(list),
        nelOpt -> nelOpt.map(NonEmptyList::toList).getOrElse(List.of()));
  }
}
