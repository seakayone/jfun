package org.kleinb.jfun.optics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Tuple;
import org.kleinb.jfun.Tuple2;

class IsoTest {

  record Person(String name, Integer age) {}

  @Test
  void testIsoLaws() {
    Iso<Person, Tuple2<String, Integer>> iso =
        Iso.of(
            (Person p) -> Tuple.of(p.name(), p.age()),
            (Tuple2<String, Integer> t) -> new Person(t._1(), t._2()));
    assertTrue(IsoLaws.roundTripOneWay(iso, new Person("Alice", 42)));
    assertTrue(IsoLaws.roundTripOtherWay(iso, Tuple.of("Alice", 42)));
  }
}
