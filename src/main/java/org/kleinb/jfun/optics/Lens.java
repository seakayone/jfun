package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function1;
import org.kleinb.jfun.Function2;

public interface Lens<WHOLE, PART> {

  static <WHOLE, PART> Lens<WHOLE, PART> of(
      Function1<WHOLE, PART> get, Function2<WHOLE, PART, WHOLE> replace) {
    return new Lens<>() {

      @Override
      public PART get(WHOLE PART) {
        return get.apply(PART);
      }

      @Override
      public WHOLE replace(WHOLE whole, PART PART) {
        return replace.apply(whole, PART);
      }
    };
  }

  PART get(WHOLE whole);

  WHOLE replace(WHOLE whole, PART b);

  default WHOLE modify(WHOLE whole, Function1<PART, PART> f) {
    return replace(whole, f.apply(get(whole)));
  }

  default <PART2> Lens<WHOLE, PART2> andThen(Lens<PART, PART2> other) {
    return new Lens<>() {

      @Override
      public PART2 get(WHOLE whole) {
        return other.get(Lens.this.get(whole));
      }

      @Override
      public WHOLE replace(WHOLE whole, PART2 part2) {
        return Lens.this.replace(whole, other.replace(Lens.this.get(whole), part2));
      }
    };
  }
}
