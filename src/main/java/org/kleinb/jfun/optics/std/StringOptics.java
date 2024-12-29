package org.kleinb.jfun.optics.std;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Iso;
import org.kleinb.jfun.optics.Prism;

public final class StringOptics {
  private StringOptics() {}

  public static Iso<String, List<Character>> stringToList() {
    return Iso.of(
        str -> {
          final List<Character> list = new ArrayList<Character>(str.length());
          for (char c : str.toCharArray()) {
            list.add(c);
          }
          return Collections.unmodifiableList(list);
        },
        list -> {
          final StringBuilder sb = new StringBuilder(list.size());
          list.forEach(sb::append);
          return sb.toString();
        });
  }

  public static Prism<String, Boolean> stringToBoolean() {
    return Prism.of(
        s ->
            switch (s) {
              case "true" -> Option.some(true);
              case "false" -> Option.some(false);
              default -> Option.none();
            },
        b -> b ? "true" : "false");
  }

  public static Prism<String, Long> stringToLong() {
    return Prism.of(StringOptics::parseToLong, Object::toString);
  }

  private static Option<Long> parseToLong(String str) {
    // reject cases where a String will break the second Prism Law
    //  * String is empty
    //  * String starts with a plus sign
    //  * String starts with a zero and has more than one digit
    final boolean inputBreaksLaws =
        str.isEmpty() || str.startsWith("+") || (str.startsWith("0") && str.length() > 1);
    if (inputBreaksLaws) {
      return Option.none();
    } else {
      return Try.of(() -> Long.parseLong(str)).toOption();
    }
  }

  public static Prism<String, Integer> stringToInteger() {
    return StringOptics.stringToLong().andThen(LongOptics.longToInteger());
  }

  public static Prism<String, Byte> stringToByte() {
    return StringOptics.stringToLong().andThen(LongOptics.longToByte());
  }

  public static Prism<String, UUID> stringToUuid() {
    return Prism.of(s -> Try.of(() -> UUID.fromString(s)).toOption(), UUID::toString);
  }

  public static Prism<String, URI> stringToUri() {
    return Prism.of(s -> Try.of(() -> new URI(s)).toOption(), URI::toString);
  }
}
