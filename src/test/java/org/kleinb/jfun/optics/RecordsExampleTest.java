package org.kleinb.jfun.optics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.Validation;

class RecordsExampleTest {

  public record Address(String address) {
    private static final Predicate<String> addressCondition = Predicate.not(String::isBlank);

    public Address {
      Objects.requireNonNull(address, "Address must not be null");
      if (addressCondition.negate().test(address)) {
        throw new IllegalArgumentException("Address must not be blank");
      }
    }

    static Validation<String, Address> of(String address) {
      return Try.of(() -> new Address(address)).toValidation().mapError(Throwable::getMessage);
    }

    static Validation<String, Option<Address>> ofNullable(String address) {
      return Option.of(address)
          .map(str -> Address.of(str).map(Option::some))
          .getOrElse(Validation.valid(Option.none()));
    }
  }

  public record Person(Name name, Age age, Option<Address> address) {

    public record Age(int age) {
      private static final Predicate<Integer> ageCondition = i -> i > 0;

      public Age {
        if (ageCondition.negate().test(age)) {
          throw new IllegalArgumentException("Age must be positive");
        }
      }

      public static Validation<String, Age> of(int age) {
        return Try.of(() -> new Age(age)).toValidation().mapError(Throwable::getMessage);
      }
    }

    public record Name(String name) {
      private static final Predicate<String> nameCondition = Predicate.not(String::isBlank);

      public Name {
        Objects.requireNonNull(name, "Name must not be null");
        if (nameCondition.negate().test(name)) {
          throw new IllegalArgumentException("Name must not be blank");
        }
      }

      public static Validation<String, Name> of(String name) {
        return Try.of(() -> new Name(name)).toValidation().mapError(Throwable::getMessage);
      }
    }

    public Person {
      Objects.requireNonNull(name, "Name must not be null");
      Objects.requireNonNull(age, "Age must not be null");
      Objects.requireNonNull(address, "Address must not be null");
    }

    public static Validation<String, Person> of(String name, int age) {
      return Validation.validateWith(
          Name.of(name), Age.of(age), Validation.valid(Option.none()), Person::new);
    }

    public static Validation<String, Person> of(String name, int age, String address) {
      return Validation.validateWith(
          Name.of(name), Age.of(age), Address.ofNullable(address), Person::new);
    }

    public Person withName(Name name) {
      return new Person(name, this.age, Option.none());
    }

    public Person withAge(Age age) {
      return new Person(this.name, age, Option.none());
    }

    public Person withAddress(Option<Address> address) {
      Objects.requireNonNull(address, "Address option must not be null");
      return new Person(this.name, this.age, address);
    }

    public Person withAddress(Address address) {
      Objects.requireNonNull(address, "Address must not be null");
      return withAddress(Option.of(address));
    }

    public Person withNoAddress() {
      return withAddress(Option.none());
    }
  }

  @Test
  void testValid() {
    assertThat(Person.of("Alice", 42, null).get())
        .isEqualTo(new Person(new Person.Name("Alice"), new Person.Age(42), Option.none()));
    assertThat(Person.of("Harry Potter", 11, "4 Privet Drive, Little Whinging, Surrey").get())
        .isEqualTo(
            new Person(
                new Person.Name("Harry Potter"),
                new Person.Age(11),
                Option.of(new Address("4 Privet Drive, Little Whinging, Surrey"))));
  }

  @Test
  void testInvalid() {
    assertThat(Person.of("Alice", -42).getError()).containsExactly("Age must be positive");
    assertThat(Person.of("", 42).getError()).containsExactly("Name must not be blank");
    assertThat(Person.of("Alice", 42, "").getError()).containsExactly("Address must not be blank");
    assertThat(Person.of("", -42, "").getError())
        .containsExactly(
            "Name must not be blank", "Age must be positive", "Address must not be blank");
  }

  @Test
  void testWith() {
    final Person person = new Person(new Person.Name("Bob"), new Person.Age(42), Option.none());
    final Address newAddress = new Address("4 Privet Drive, Little Whinging, Surrey");

    assertThat(person.withName(new Person.Name("Bob")))
        .isEqualTo(new Person(new Person.Name("Bob"), new Person.Age(42), Option.none()));

    assertThat(person.withAge(new Person.Age(43)))
        .isEqualTo(new Person(new Person.Name("Alice"), new Person.Age(43), Option.none()));

    assertThat(person.withAddress(newAddress))
        .isEqualTo(new Person(new Person.Name("Alice"), new Person.Age(42), Option.of(newAddress)));
    assertThat(person.withNoAddress())
        .isEqualTo(new Person(new Person.Name("Alice"), new Person.Age(42), Option.none()));
    assertThat(person.withAddress(Option.of(newAddress)))
        .isEqualTo(new Person(new Person.Name("Alice"), new Person.Age(42), Option.of(newAddress)));
  }
}
