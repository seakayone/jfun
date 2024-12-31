package org.kleinb.jfun;

import java.util.NoSuchElementException;

public interface Iterator<A> extends java.util.Iterator<A> {
  @SuppressWarnings("unchecked")
  static <A> Iterator<A> empty() {
    return (Iterator<A>) EmptyIterator.INSTANCE;
  }

  static <A> Iterator<A> of(A elem) {
    return new SingleElementIterator<>(elem);
  }
}

final class EmptyIterator<A> implements Iterator<A> {
  private EmptyIterator() {}

  static final EmptyIterator<?> INSTANCE = new EmptyIterator<>();

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public A next() {
    throw new NoSuchElementException();
  }
}

final class SingleElementIterator<A> implements Iterator<A> {
  private final A elem;
  private boolean hasNext = true;

  SingleElementIterator(A elem) {
    this.elem = elem;
  }

  @Override
  public boolean hasNext() {
    return hasNext;
  }

  @Override
  public A next() {
    if (hasNext()) {
      this.hasNext = false;
      return elem;
    } else {
      throw new NoSuchElementException();
    }
  }
}
