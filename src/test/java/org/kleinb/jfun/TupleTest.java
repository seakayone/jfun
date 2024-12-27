package org.kleinb.jfun;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TupleTest {

  @Test
  void empty() {
    Tuple0<?> t = Tuple.empty();
    assertThat(t).isSameAs(Tuple0.INSTANCE);
  }

  @Test
  void tuple1() {
    Tuple1<String> t = Tuple.of("foo");
    assertThat(t._1()).isEqualTo("foo");
  }

  @Test
  void tuple2() {
    Tuple2<String, Integer> t = Tuple.of("foo", 42);
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
  }

  @Test
  void tuple3() {
    Tuple3<String, Integer, Boolean> t = Tuple.of("foo", 42, true);
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
    assertThat(t._3()).isTrue();
  }

  @Test
  void tuple4() {
    Tuple4<String, Integer, Boolean, Double> t = Tuple.of("foo", 42, true, 3.14);
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
    assertThat(t._3()).isTrue();
    assertThat(t._4()).isEqualTo(3.14);
  }

  @Test
  void tuple5() {
    Tuple5<String, Integer, Boolean, Double, Long> t = Tuple.of("foo", 42, true, 3.14, 42L);
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
    assertThat(t._3()).isTrue();
    assertThat(t._4()).isEqualTo(3.14);
    assertThat(t._5()).isEqualTo(42L);
  }

  @Test
  void tuple6() {
    Tuple6<String, Integer, Boolean, Double, Long, Short> t =
        Tuple.of("foo", 42, true, 3.14, 42L, (short) 42);
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
    assertThat(t._3()).isTrue();
    assertThat(t._4()).isEqualTo(3.14);
    assertThat(t._5()).isEqualTo(42L);
    assertThat(t._6()).isEqualTo((short) 42);
  }

  @Test
  void tuple7() {
    Tuple7<String, Integer, Boolean, Double, Long, Short, Byte> t =
        Tuple.of("foo", 42, true, 3.14, 42L, (short) 42, (byte) 42);
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
    assertThat(t._3()).isTrue();
    assertThat(t._4()).isEqualTo(3.14);
    assertThat(t._5()).isEqualTo(42L);
    assertThat(t._6()).isEqualTo((short) 42);
    assertThat(t._7()).isEqualTo((byte) 42);
  }

  @Test
  void tuple8() {
    Tuple8<String, Integer, Boolean, Double, Long, Short, Byte, Character> t =
        Tuple.of("foo", 42, true, 3.14, 42L, (short) 42, (byte) 42, 'c');
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
    assertThat(t._3()).isTrue();
    assertThat(t._4()).isEqualTo(3.14);
    assertThat(t._5()).isEqualTo(42L);
    assertThat(t._6()).isEqualTo((short) 42);
    assertThat(t._7()).isEqualTo((byte) 42);
    assertThat(t._8()).isEqualTo('c');
  }

  @Test
  void fromEntry() {
    Tuple2<String, Integer> t = Tuple.fromEntry(Map.entry("foo", 42));
    assertThat(t._1()).isEqualTo("foo");
    assertThat(t._2()).isEqualTo(42);
  }

  @Test
  void toMap() {
    Map<String, Integer> map = Tuple.toMap(List.of(Tuple.of("foo", 42), Tuple.of("bar", 43)));
    assertThat(map).containsEntry("foo", 42).containsEntry("bar", 43);
  }
}
