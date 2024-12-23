package org.kleinb.jfun;

public record Failure<A>(Throwable error) implements Try<A> {

}
