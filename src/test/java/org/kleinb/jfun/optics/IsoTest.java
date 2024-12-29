package org.kleinb.jfun.optics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Tuple;
import org.kleinb.jfun.Tuple2;
import org.kleinb.jfun.optics.law.IsoLaws;

class IsoTest {

  record Person(String name, Integer age) {}

  final Iso<Person, Tuple2<String, Integer>> personTuple2Iso =
      Iso.of(p -> Tuple.of(p.name(), p.age()), t -> new Person(t._1(), t._2()));

  @Test
  void testIsoLaws() {
    assertThat(IsoLaws.roundTripOneWay(personTuple2Iso, new Person("Alice", 42))).isTrue();
    assertThat(IsoLaws.roundTripOtherWay(personTuple2Iso, Tuple.of("Alice", 42))).isTrue();
  }

  // .replace

  @Test
  void replace() {
    Person person = new Person("Alice", 42);
    assertThat(personTuple2Iso.replace(Tuple.of("Bob", 43)).apply(person))
        .isEqualTo(new Person("Bob", 43));
  }

  // .asLens

  @Test
  void asLens() {
    assertThat(personTuple2Iso.asLens()).isSameAs(personTuple2Iso);
  }

  // .unit

  @Test
  void unit() {
    Iso<Integer, Void> iso = Iso.unit(() -> 42);
    assertThat(iso.get(42)).isNull();
    assertThat(iso.reverseGet(null)).isEqualTo(42);
  }

  // .identity

  @Test
  void identity() {
    Iso<Person, Person> iso = Iso.identity();
    Person person = new Person("Alice", 42);
    assertThat(iso.get(person)).isSameAs(person);
    assertThat(iso.reverseGet(person)).isSameAs(person);
  }
}
