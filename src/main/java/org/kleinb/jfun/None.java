package org.kleinb.jfun;

public record None<A>() implements Option<A> {
  public static final None<?> INSTANCE = new None<>();
}
