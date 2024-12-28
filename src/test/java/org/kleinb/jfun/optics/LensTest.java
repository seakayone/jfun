package org.kleinb.jfun.optics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LensTest {

  record City(String cityName) {}

  record Address(City city, String street) {}

  record Person(String name, Address address) {}

  final Lens<Person, String> nameLens =
      Lens.of(Person::name, (person, name) -> new Person(name, person.address()));
  final Lens<Person, Address> addressLens =
      Lens.of(Person::address, (person, address) -> new Person(person.name(), address));

  final Lens<Address, City> cityLens =
      Lens.of(Address::city, (address, city) -> new Address(city, address.street()));

  final Lens<City, String> cityNameLens =
      Lens.of(City::cityName, (_, cityName) -> new City(cityName));

  final Lens<Person, String> personCityLens = addressLens.andThen(cityLens).andThen(cityNameLens);

  @Test
  void shouldSatisfyLensLaws() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(LensLaws.getReplace(nameLens, person)).isTrue();
    assertThat(LensLaws.replaceGet(nameLens, person, "Holmes")).isTrue();
  }

  @Test
  void shouldGet() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(nameLens.get(person)).isEqualTo("Sherlock");
  }

  @Test
  void shouldReplace() {
    final Address address = new Address(new City("London"), "Baker Street");
    final Person person = new Person("Sherlock", address);

    final Person actual = nameLens.replace(person, "Holmes");

    assertThat(actual.name()).isEqualTo("Holmes");
    assertThat(actual.address()).isEqualTo(address);
  }

  @Test
  void shouldComposeGet() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.get(person)).isEqualTo("London");
  }

  @Test
  void shouldComposeReplace() {
    final Address address = new Address(new City("London"), "Baker Street");
    final Person person = new Person("Sherlock", address);

    final Person actual = personCityLens.replace(person, "Paris");

    assertThat(actual.name()).isEqualTo("Sherlock");
    assertThat(actual.address()).isEqualTo(new Address(new City("Paris"), "Baker Street"));
  }

  @Test
  void shouldModify() {
    final Address address = new Address(new City("London"), "Baker Street");
    final Person person = new Person("Sherlock", address);

    final Person actual = personCityLens.modify(person, String::toUpperCase);

    assertThat(actual)
        .isEqualTo(new Person("Sherlock", new Address(new City("LONDON"), "Baker Street")));
  }
}
