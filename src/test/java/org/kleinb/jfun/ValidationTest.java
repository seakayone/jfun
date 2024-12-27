package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ValidationTest {

  // factory methods, construction

  @Test
  void shouldCreateValid() {
    Validation<?, Integer> actual = Validation.valid(42);
    assertThat(actual).isInstanceOf(Valid.class);
  }

  @Test
  void shouldCreateInvalid() {
    Validation<String, ?> actual = Validation.invalid("error");
    assertThat(actual).isInstanceOf(Invalid.class);
  }

  @Test
  void shouldCreateFromRight() {
    Validation<String, Integer> actual = Validation.fromEither(Either.right(42));
    assertThat(actual).isInstanceOf(Valid.class);
  }

  @Test
  void shouldCreateFromLeft() {
    Validation<String, Integer> actual = Validation.fromEither(Either.left("error"));
    assertThat(actual).isEqualTo(Validation.invalid("error"));
  }

  @Test
  void shouldCreateFromTrySuccess() {
    Validation<Throwable, Integer> actual = Validation.fromTry(Try.success(42));
    assertThat(actual).isEqualTo(Validation.valid(42));
  }

  @Test
  void shouldCreateFromTryFailure() {
    Exception exception = new RuntimeException("error");
    Validation<Throwable, Integer> actual = Validation.fromTry(Try.failure(exception));
    assertThat(actual).isEqualTo(Validation.invalid(exception));
  }

  @Test
  void shouldCreateFromPredicate() {
    Validation<String, Integer> actual =
        Validation.fromPredicate("Not greater than zero", i -> i > 0, 42);
    assertThat(actual).isEqualTo(Validation.valid(42));
  }

  @Test
  void shouldCreateFromPredicateInvalid() {
    Validation<String, Integer> actual =
        Validation.fromPredicate("Not greater than zero", i -> i > 0, -42);
    assertThat(actual).isEqualTo(Validation.invalid("Not greater than zero"));
  }

  // .isValid , .isInvalid
  @Test
  void shouldCheckValid() {
    Validation<?, Integer> valid = Validation.valid(42);
    assertThat(valid.isValid()).isTrue();
    assertThat(valid.isInvalid()).isFalse();
  }

  @Test
  void shouldCheckInvalid() {
    Validation<String, ?> invalid = Validation.invalid("error");
    assertThat(invalid.isValid()).isFalse();
    assertThat(invalid.isInvalid()).isTrue();
  }

  // .get

  @Test
  void shouldGetValidValue() {
    Validation<?, Integer> valid = Validation.valid(42);
    assertThat(valid.get()).isEqualTo(42);
  }

  @Test
  void shouldThrowOnGetInvalid() {
    Validation<String, ?> invalid = Validation.invalid("error");
    assertThatThrownBy(invalid::get).isInstanceOf(NoSuchElementException.class);
  }

  // .zip

  @Test
  void shouldZipValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    Validation<String, String> other = Validation.valid("foo");
    assertThat(valid.zip(other)).isEqualTo(Validation.valid(Tuple.of(42, "foo")));
  }

  @Test
  void shouldZipInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    Validation<String, String> other = Validation.valid("foo");
    assertThat(invalid.zip(other)).isEqualTo(Validation.invalid("error"));
    assertThat(other.zip(invalid)).isEqualTo(Validation.invalid("error"));
  }

  @Test
  void shouldZipInvalidInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    Validation<String, String> other = Validation.invalid("error2");
    assertThat(invalid.zip(other)).isEqualTo(Validation.invalid(List.of("error", "error2")));
  }

  // .getError

  @Test
  void shouldGetInvalidError() {
    Validation<String, ?> invalid = Validation.invalid("error");
    assertThat(invalid.getError()).isEqualTo(List.of("error"));
  }

  @Test
  void shouldThrowOnGetErrorValid() {
    Validation<?, Integer> valid = Validation.valid(42);
    assertThatThrownBy(valid::getError).isInstanceOf(NoSuchElementException.class);
  }

  // .errors

  @Test
  void errors() {
    Validation<String, Integer> invalid1 = Validation.invalid("error1");
    Validation<String, Double> invalid2 = Validation.invalid("error2");
    Validation<String, String> valid = Validation.valid("valid");
    assertThat(Validation.errors(List.of(invalid1, invalid2, valid)))
        .isEqualTo(List.of("error1", "error2"));
  }

  @Test
  void errorsStream() {
    Validation<String, Integer> invalid1 = Validation.invalid("error1");
    Validation<String, Double> invalid2 = Validation.invalid("error2");
    Validation<String, String> valid = Validation.valid("valid");
    assertThat(Validation.errors(Stream.of(invalid1, invalid2, valid)))
        .isEqualTo(List.of("error1", "error2"));
  }

  // .getOrElse

  @Test
  void shouldGetOrElseValid() {
    Validation<?, Integer> valid = Validation.valid(42);
    assertThat(valid.getOrElse(0)).isEqualTo(42);
  }

  @Test
  void shouldGetOrElseInvalid() {
    Validation<String, String> invalid = Validation.invalid("error");
    assertThat(invalid.getOrElse("42")).isEqualTo("42");
  }

  @Test
  void shouldGetOrElseSupplierValid() {
    Validation<?, Integer> valid = Validation.valid(42);
    assertThat(valid.getOrElse(() -> 0)).isEqualTo(42);
  }

  @Test
  void shouldGetOrElseSupplierInvalid() {
    Validation<String, String> invalid = Validation.invalid("error");
    assertThat(invalid.getOrElse(() -> "42")).isEqualTo("42");
  }

  // .orElse

  @Test
  void shouldReturnValidOnOrElseValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    Validation<String, Integer> or = Validation.invalid("error");
    assertThat(valid.orElse(or)).isEqualTo(valid);
  }

  @Test
  void shouldReturnOrOnOrElseInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    Validation<String, Integer> or = Validation.valid(42);
    assertThat(invalid.orElse(or)).isEqualTo(or);
  }

  @Test
  void shouldReturnValidOnOrElseSupplierValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    Validation<String, Integer> or = Validation.invalid("error");
    assertThat(valid.orElse(() -> or)).isEqualTo(valid);
  }

  @Test
  void shouldReturnOrOnOrElseSupplierInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    Validation<String, Integer> or = Validation.valid(42);
    assertThat(invalid.orElse(() -> or)).isEqualTo(or);
  }

  // .swap

  @Test
  void shouldSwapValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.swap()).isEqualTo(Validation.invalid(42));
  }

  @Test
  void shouldSwapInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    assertThat(invalid.swap()).isEqualTo(Validation.valid(List.of("error")));
  }

  // .map

  @Test
  void shouldMapValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.map(String::valueOf)).isEqualTo(Validation.valid("42"));
  }

  @Test
  void shouldMapInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    assertThat(invalid.map(String::valueOf)).isEqualTo(Validation.invalid("error"));
  }

  // .mapError

  @Test
  void shouldMapErrorValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.mapError(String::length)).isEqualTo(Validation.valid(42));
  }

  @Test
  void shouldMapErrorInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    assertThat(invalid.mapError(String::length)).isEqualTo(Validation.invalid(5));
  }

  // .flatMap

  @Test
  void shouldFlatMapValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.flatMap(i -> Validation.valid(String.valueOf(i))))
        .isEqualTo(Validation.valid("42"));
  }

  @Test
  void shouldFlatMapInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    assertThat(invalid.flatMap(i -> Validation.valid(String.valueOf(i))))
        .isEqualTo(Validation.invalid("error"));
  }

  @Test
  void shouldFlatMapValidInvalid() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.flatMap(_ -> Validation.invalid("error")))
        .isEqualTo(Validation.invalid("error"));
  }

  // .fold
  @Test
  void shouldFoldValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    Integer actual = valid.fold(List::size, i -> i);
    assertThat(actual).isEqualTo(42);
  }

  @Test
  void shouldFoldInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    Integer actual = invalid.fold(List::size, i -> i);
    assertThat(actual).isEqualTo(1);
  }

  // .tap

  @Test
  void shouldTapValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    StringBuilder sb = new StringBuilder();
    var actual = valid.tap(sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(valid);
  }

  @Test
  void shouldNotTapInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    StringBuilder sb = new StringBuilder();
    var actual = invalid.tap(sb::append);
    assertThat(sb.toString()).isEmpty();
    assertThat(actual).isEqualTo(invalid);
  }

  // .tapError

  @Test
  void shouldTapInvalidValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    StringBuilder sb = new StringBuilder();
    var actual = valid.tapInvalid(sb::append);
    assertThat(sb.toString()).isEmpty();
    assertThat(actual).isEqualTo(valid);
  }

  @Test
  void shouldTapInvalidInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    StringBuilder sb = new StringBuilder();
    var actual = invalid.tapInvalid(it -> sb.append(it.size()));
    assertThat(sb.toString()).isEqualTo("1");
    assertThat(actual).isEqualTo(invalid);
  }

  // .

  @Test
  void shouldTapBothValid() {
    Validation<String, Integer> valid = Validation.valid(42);
    StringBuilder sb = new StringBuilder();
    var actual = valid.tapBoth(sb::append, sb::append);
    assertThat(sb.toString()).isEqualTo("42");
    assertThat(actual).isEqualTo(valid);
  }

  @Test
  void shouldTapBothInvalid() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    StringBuilder sb = new StringBuilder();
    var actual = invalid.tapBoth(it -> sb.append(it.size()), sb::append);
    assertThat(sb.toString()).isEqualTo("1");
    assertThat(actual).isEqualTo(invalid);
  }

  // .validateWith

  @Test
  void shouldValidateWithValid() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), "John Doe"),
            Validation.fromPredicate("Age is negative", i -> i > 0, 42),
            Tuple::of);
    assertThat(actual).isEqualTo(Validation.valid(Tuple.of("John Doe", 42)));
  }

  @Test
  void shouldRetainAllErrorsValidateWithInvalid() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), ""),
            Validation.fromPredicate("Age is negative", i -> i > 0, -42),
            Tuple::of);
    assertThat(actual).isEqualTo(Validation.invalid(List.of("Name is empty", "Age is negative")));
  }

  @Test
  void shouldValidateWithValid3() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), "John Doe"),
            Validation.fromPredicate("Age is negative", i -> i > 0, 42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a@bL"),
            Tuple::of);
    assertThat(actual).isEqualTo(Validation.valid(Tuple.of("John Doe", 42, "a@bL")));
  }

  @Test
  void shouldRetainAllErrorsValidateWithInvalid3() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), ""),
            Validation.fromPredicate("Age is negative", i -> i > 0, -42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a"),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.invalid(List.of("Name is empty", "Age is negative", "Email is invalid")));
  }

  @Test
  void shouldValidateWithValid4() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), "John Doe"),
            Validation.fromPredicate("Age is negative", i -> i > 0, 42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a@bL"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "+123"),
            Tuple::of);
    assertThat(actual).isEqualTo(Validation.valid(Tuple.of("John Doe", 42, "a@bL", "+123")));
  }

  @Test
  void shouldRetainAllErrorsValidateWithInvalid4() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), ""),
            Validation.fromPredicate("Age is negative", i -> i > 0, -42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "123"),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.invalid(
                List.of(
                    "Name is empty", "Age is negative", "Email is invalid", "Phone is invalid")));
  }

  @Test
  void shouldValidateWithValid5() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), "John Doe"),
            Validation.fromPredicate("Age is negative", i -> i > 0, 42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a@bL"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "+123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "DE"),
            Tuple::of);
    assertThat(actual).isEqualTo(Validation.valid(Tuple.of("John Doe", 42, "a@bL", "+123", "DE")));
  }

  @Test
  void shouldRetainAllErrorsValidateWithInvalid5() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), ""),
            Validation.fromPredicate("Age is negative", i -> i > 0, -42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "D"),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.invalid(
                List.of(
                    "Name is empty",
                    "Age is negative",
                    "Email is invalid",
                    "Phone is invalid",
                    "Country is invalid")));
  }

  @Test
  void shouldValidateWithValid6() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), "John Doe"),
            Validation.fromPredicate("Age is negative", i -> i > 0, 42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a@bL"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "+123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "DE"),
            Validation.fromPredicate("City is invalid", s -> s.length() > 2, "Berlin"),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(Validation.valid(Tuple.of("John Doe", 42, "a@bL", "+123", "DE", "Berlin")));
  }

  @Test
  void shouldRetainAllErrorsValidateWithInvalid6() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), ""),
            Validation.fromPredicate("Age is negative", i -> i > 0, -42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "D"),
            Validation.fromPredicate("City is invalid", s -> s.length() > 2, "B"),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.invalid(
                List.of(
                    "Name is empty",
                    "Age is negative",
                    "Email is invalid",
                    "Phone is invalid",
                    "Country is invalid",
                    "City is invalid")));
  }

  @Test
  void shouldValidateWithValid7() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), "John Doe"),
            Validation.fromPredicate("Age is negative", i -> i > 0, 42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a@bL"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "+123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "DE"),
            Validation.fromPredicate("City is invalid", s -> s.length() > 2, "Berlin"),
            Validation.fromPredicate("Street is invalid", s -> s.length() > 3, "Karl-Marx-Str."),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.valid(
                Tuple.of("John Doe", 42, "a@bL", "+123", "DE", "Berlin", "Karl-Marx-Str.")));
  }

  @Test
  void shouldRetainAllErrorsValidateWithInvalid7() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), ""),
            Validation.fromPredicate("Age is negative", i -> i > 0, -42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "D"),
            Validation.fromPredicate("City is invalid", s -> s.length() > 2, "B"),
            Validation.fromPredicate("Street is invalid", s -> s.length() > 3, ""),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.invalid(
                List.of(
                    "Name is empty",
                    "Age is negative",
                    "Email is invalid",
                    "Phone is invalid",
                    "Country is invalid",
                    "City is invalid",
                    "Street is invalid")));
  }

  @Test
  void shouldValidateWithValid8() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), "John Doe"),
            Validation.fromPredicate("Age is negative", i -> i > 0, 42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a@bL"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "+123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "DE"),
            Validation.fromPredicate("City is invalid", s -> s.length() > 2, "Berlin"),
            Validation.fromPredicate("Street is invalid", s -> s.length() > 3, "Karl-Marx-Str."),
            Validation.fromPredicate("Zip is invalid", s -> s.length() == 5, "12345"),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.valid(
                Tuple.of(
                    "John Doe", 42, "a@bL", "+123", "DE", "Berlin", "Karl-Marx-Str.", "12345")));
  }

  @Test
  void shouldRetainAllErrorsValidateWithInvalid8() {
    var actual =
        Validation.validateWith(
            Validation.fromPredicate("Name is empty", Predicate.not(String::isEmpty), ""),
            Validation.fromPredicate("Age is negative", i -> i > 0, -42),
            Validation.fromPredicate("Email is invalid", s -> s.contains("@"), "a"),
            Validation.fromPredicate("Phone is invalid", s -> s.startsWith("+"), "123"),
            Validation.fromPredicate("Country is invalid", s -> s.length() == 2, "D"),
            Validation.fromPredicate("City is invalid", s -> s.length() > 2, "B"),
            Validation.fromPredicate("Street is invalid", s -> s.length() > 3, ""),
            Validation.fromPredicate("Zip is invalid", s -> s.length() == 5, "1234"),
            Tuple::of);
    assertThat(actual)
        .isEqualTo(
            Validation.invalid(
                List.of(
                    "Name is empty",
                    "Age is negative",
                    "Email is invalid",
                    "Phone is invalid",
                    "Country is invalid",
                    "City is invalid",
                    "Street is invalid",
                    "Zip is invalid")));
  }

  // .toEither

  @Test
  void shouldConvertValidToRight() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.toEither()).isEqualTo(Either.right(42));
  }

  @Test
  void shouldConvertInvalidToLeft() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    assertThat(invalid.toEither()).isEqualTo(Either.left(List.of("error")));
  }

  // .toOption

  @Test
  void shouldConvertValidToSome() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.toOption()).isEqualTo(Option.some(42));
  }

  @Test
  void shouldConvertInvalidToNone() {
    Validation<String, Integer> invalid = Validation.invalid("error");
    assertThat(invalid.toOption()).isEqualTo(Option.none());
  }

  // .toTry

  @Test
  void shouldConvertValidToSuccess() {
    Validation<String, Integer> valid = Validation.valid(42);
    assertThat(valid.toTryWith(_ -> new RuntimeException())).isEqualTo(Try.success(42));
  }

  @Test
  void shouldConvertInvalidToFailure() {
    var exception = new RuntimeException("error");
    Validation<String, Integer> invalid = Validation.invalid("error");
    assertThat(invalid.toTryWith(_ -> exception)).isEqualTo(Try.failure(exception));
  }
}
