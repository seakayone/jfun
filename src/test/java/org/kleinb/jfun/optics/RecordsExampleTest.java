package org.kleinb.jfun.optics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kleinb.jfun.Validation.valid;

import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.Validation;

class RecordsExampleTest {

  public record Id(UUID value) {
    public Id {
      Objects.requireNonNull(value, "Id must not be null");
    }

    public static Validation<String, Id> of(String value) {
      return Try.of(() -> UUID.fromString(value))
          .map(Id::new)
          .toValidation()
          .mapError(Throwable::getMessage);
    }

    public static Id makeNew() {
      return new Id(UUID.randomUUID());
    }

    @Override
    public String toString() {
      return value.toString();
    }
  }

  public record Age(int age) {
    public Age {
      if (age < 0) {
        throw new IllegalArgumentException("Age must be positive");
      }
    }

    public static Validation<String, Age> of(int age) {
      return Try.of(() -> new Age(age)).toValidation().mapError(Throwable::getMessage);
    }
  }

  public record Name(String name) {
    public Name {
      Objects.requireNonNull(name, "Name must not be null");
      if (name.isBlank()) {
        throw new IllegalArgumentException("Name must not be blank");
      }
    }

    public static Validation<String, Name> of(String name) {
      return Try.of(() -> new Name(name)).toValidation().mapError(Throwable::getMessage);
    }
  }

  public record Address(String address) {
    public Address {
      Objects.requireNonNull(address, "Address must not be null");
      if (address.isBlank()) {
        throw new IllegalArgumentException("Address must not be blank");
      }
    }

    static Validation<String, Address> of(String address) {
      return Try.of(() -> new Address(address)).toValidation().mapError(Throwable::getMessage);
    }

    static Validation<String, Option<Address>> ofNullable(String address) {
      return Option.of(address)
          .map(str -> Address.of(str).map(Option::some))
          .getOrElse(Validation.none());
    }
  }

  public record Person(Id id, Name name, Age age, Option<Address> address) {
    public Person {
      Objects.requireNonNull(name, "Name must not be null");
      Objects.requireNonNull(age, "Age must not be null");
      Objects.requireNonNull(address, "Address must not be null");
    }

    public static Validation<String, Person> of(String id, String name, int age) {
      return Validation.validateWith(
          Id.of(id), Name.of(name), Age.of(age), Validation.none(), Person::new);
    }

    public static Validation<String, Person> of(String id, String name, int age, String address) {
      return Validation.validateWith(
          Id.of(id), Name.of(name), Age.of(age), Address.ofNullable(address), Person::new);
    }

    static Lens<Person, Name> nameLens =
        Lens.of(p -> p.name, (name, p) -> new Person(p.id, name, p.age, p.address));

    static Lens<Person, Age> ageLens =
        Lens.of(p -> p.age, (age, p) -> new Person(p.id, p.name, age, p.address));

    static Lens<Person, Option<Address>> addressLens =
        Lens.of(p -> p.address, (address, p) -> new Person(p.id, p.name, p.age, address));

    public Person withName(Name name) {
      Objects.requireNonNull(name, "Name must not be null");
      return Person.nameLens.replace(name).apply(this);
    }

    public Person withAge(Age age) {
      return Person.ageLens.replace(age).apply(this);
    }

    public Person withAddress(Option<Address> address) {
      Objects.requireNonNull(address, "Address option must not be null");
      return Person.addressLens.replace(address).apply(this);
    }
  }

  @Test
  void testValid() {
    final var id = Id.makeNew();
    assertThat(Person.of(id.toString(), "Alice", 42, null))
        .isEqualTo(valid(new Person(id, new Name("Alice"), new Age(42), Option.none())));
    assertThat(
            Person.of(id.toString(), "Harry Potter", 11, "4 Privet Drive, Little Whinging, Surrey"))
        .isEqualTo(
            valid(
                new Person(
                    id,
                    new Name("Harry Potter"),
                    new Age(11),
                    Option.of(new Address("4 Privet Drive, Little Whinging, Surrey")))));
  }

  @Test
  void testInvalid() {
    final var id = Id.makeNew().toString();
    assertThat(Person.of(id, "Alice", -42).getError()).containsExactly("Age must be positive");
    assertThat(Person.of(id, "", 42).getError()).containsExactly("Name must not be blank");
    assertThat(Person.of(id, "Alice", 42, "").getError())
        .containsExactly("Address must not be blank");
    assertThat(Person.of(id, "", -42, "").getError())
        .containsExactly(
            "Name must not be blank", "Age must be positive", "Address must not be blank");
  }

  @Test
  void testWith() {
    final var id = Id.makeNew();
    final Person p = new Person(id, new Name("Bob"), new Age(42), Option.none());
    final Address newAddress = new Address("4 Privet Drive, Little Whinging, Surrey");

    assertThat(p.withAge(new Age(43))).isEqualTo(new Person(id, p.name, new Age(43), p.address));
    assertThat(p.withName(new Name("Bob")))
        .isEqualTo(new Person(id, new Name("Bob"), p.age, p.address));
    assertThat(p.withAddress(Option.of(newAddress)))
        .isEqualTo(new Person(id, p.name, p.age, Option.of(newAddress)));
  }
}
