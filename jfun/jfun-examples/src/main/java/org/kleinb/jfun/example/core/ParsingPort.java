package org.kleinb.jfun.example.core;

import org.kleinb.jfun.Try;

/** Parses a port number with {@link Try} and handles both outcomes with pattern matching. */
public class ParsingPort {

  static String describe(String text) {
    Try<Integer> port = Try.of(() -> Integer.parseInt(text));

    return switch (port) {
      case Try.Success<Integer>(var value) -> "Listening on " + value;
      case Try.Failure<Integer>(var error) -> "Invalid port: " + error.getMessage();
    };
  }

  public static void main(String[] args) {
    System.out.println(describe("8080"));
    System.out.println(describe("http"));
    System.out.println(describe(null));
  }
}
