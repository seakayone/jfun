package org.kleinb.jfun.example.optics;

import static java.util.function.Predicate.not;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import org.kleinb.jfun.*;

public class Zooming {

  private static final Predicate<String> nullOrBlank = s -> s == null || s.isBlank();
  private static final Predicate<Integer> positiveInt = i -> i > 0;

  record PersonCreateRequest(String givenName, String familyName, int age) {}

  record CompanyCreateRequest(String companyName) {}

  record Id(UUID value) {
    public Id {
      Objects.requireNonNull(value, "Id must not be null");
    }

    public static Id makeNew() {
      return new Id(UUID.randomUUID());
    }

    public static Validation<String, Id> of(String id) {
      return Try.of(() -> Objects.requireNonNull(id))
          .mapTry(UUID::fromString)
          .toValidation()
          .mapError(_ -> "Id must be a valid UUID")
          .map(Id::new);
    }
  }

  record Age(int value) {
    public Age {
      if (!positiveInt.test(value)) {
        throw new IllegalArgumentException("Age must not be negative");
      }
    }

    public static Validation<String, Age> of(int age) {
      return Validation.validateWith(
          Validation.fromPredicate("Age must not be negative", positiveInt, age), Age::new);
    }
  }

  record PersonName(String givenName, String familyName) {

    PersonName {
      if (nullOrBlank.test(givenName)) {
        throw new IllegalArgumentException();
      }
      if (nullOrBlank.test(familyName)) {
        throw new IllegalArgumentException();
      }
    }

    static Validation<String, PersonName> of(String givenName, String familyName) {
      return Validation.validateWith(
          Validation.fromPredicate("Given name must not be blank", not(nullOrBlank), givenName),
          Validation.fromPredicate("Family name must not be blank", not(nullOrBlank), familyName),
          PersonName::new);
    }
  }

  record CompanyName(String value) {
    CompanyName {
      if (nullOrBlank.test(value)) {
        throw new IllegalArgumentException("Name must not be blank");
      }
    }

    static Validation<String, CompanyName> of(String name) {
      return Validation.validateWith(
          Validation.fromPredicate("Name must not be blank", not(nullOrBlank), name),
          CompanyName::new);
    }
  }

  sealed interface Customer {
    record Person(Id id, PersonName name, Age age) implements Customer {
      public Person {
        Objects.requireNonNull(name, "Name must not be null");
        Objects.requireNonNull(id, "Id must not be null");
      }
    }

    record Company(Id id, CompanyName name) implements Customer {
      public Company {
        Objects.requireNonNull(name, "Name must not be null");
        Objects.requireNonNull(id, "Id must not be null");
      }
    }

    static Validation<String, Person> person(PersonCreateRequest req) {
      return Validation.validateWith(
          Validation.valid(Id.makeNew()),
          PersonName.of(req.givenName, req.familyName),
          Age.of(req.age),
          Person::new);
    }

    static Validation<String, Company> company(CompanyCreateRequest req) {
      return Validation.validateWith(
          Validation.valid(Id.makeNew()), CompanyName.of(req.companyName), Company::new);
    }
  }
}
