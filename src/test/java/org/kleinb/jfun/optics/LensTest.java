package org.kleinb.jfun.optics;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Either;
import org.kleinb.jfun.Option;

class LensTest {

  record City(String cityName) {}

  record Address(City city, String street) {}

  record Person(String name, Address address) {}

  final Lens<Person, String> nameLens =
      Lens.of(Person::name, (name, person) -> new Person(name, person.address()));
  final Lens<Person, Address> addressLens =
      Lens.of(Person::address, (address, person) -> new Person(person.name(), address));

  final Lens<Address, City> cityLens =
      Lens.of(Address::city, (city, address) -> new Address(city, address.street()));

  final Lens<City, String> cityNameLens =
      Lens.of(City::cityName, (cityName, _) -> new City(cityName));

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

  // .replace

  @Test
  void shouldReplace() {
    final Address address = new Address(new City("London"), "Baker Street");
    final Person person = new Person("Sherlock", address);

    final Person actual = nameLens.replace("Holmes").apply(person);

    assertThat(actual.name()).isEqualTo("Holmes");
    assertThat(actual.address()).isEqualTo(address);
  }

  // .andThen, compose

  @Test
  void shouldComposeGet() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.get(person)).isEqualTo("London");
  }

  @Test
  void shouldComposeReplace() {
    final Address address = new Address(new City("London"), "Baker Street");
    final Person person = new Person("Sherlock", address);

    final Person actual = personCityLens.replace("Paris").apply(person);

    assertThat(actual.name()).isEqualTo("Sherlock");
    assertThat(actual.address()).isEqualTo(new Address(new City("Paris"), "Baker Street"));
  }

  @Test
  void shouldSatisfyLensLawsComposed() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(LensLaws.getReplace(personCityLens, person)).isTrue();
    assertThat(LensLaws.replaceGet(personCityLens, person, "London")).isTrue();
  }

  // .modify

  @Test
  void shouldModify() {
    final Address address = new Address(new City("London"), "Baker Street");
    final Person person = new Person("Sherlock", address);

    final Person actual = personCityLens.modify(String::toUpperCase).apply(person);

    assertThat(actual)
        .isEqualTo(new Person("Sherlock", new Address(new City("LONDON"), "Baker Street")));
  }

  // .getOrModify

  @Test
  void shouldGetOrModify() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.getOrModify(person)).isEqualTo(Either.right("London"));
  }

  // .getOption

  @Test
  void shouldGetOption() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.getOption(person)).isEqualTo(Option.some("London"));
  }

  // .find

  @Test
  void shouldFind() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.find("London"::equals).apply(person))
        .isEqualTo(Option.some("London"));
  }

  @Test
  void shouldFindNoMatch() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.find("Paris"::equals).apply(person)).isEqualTo(Option.none());
  }

  // exists

  @Test
  void shouldExist() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.exists("London"::equals).test(person)).isTrue();
  }

  @Test
  void shouldNotExist() {
    final Person person = new Person("Sherlock", new Address(new City("London"), "Baker Street"));
    assertThat(personCityLens.exists("Paris"::equals).test(person)).isFalse();
  }
}
