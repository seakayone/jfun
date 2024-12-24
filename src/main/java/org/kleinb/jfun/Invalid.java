package org.kleinb.jfun;


import java.util.List;

public record Invalid<E, A>(List<E> error) implements Validation<E, A> {
}
