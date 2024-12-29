package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.Iso;
import org.kleinb.jfun.optics.Prism;
import org.kleinb.jfun.optics.law.IsoLaws;
import org.kleinb.jfun.optics.law.PrismLaws;

class StringOpticsTest {

  @Test
  void stringToListIsoLaws() {
    final Iso<String, List<Character>> iso = StringOptics.stringToList();

    assertThat(IsoLaws.roundTripOneWay(iso, "abc")).isTrue();
    assertThat(IsoLaws.roundTripOneWay(iso, "")).isTrue();

    assertThat(IsoLaws.roundTripOtherWay(iso, List.of('a', 'b', 'c'))).isTrue();
    assertThat(IsoLaws.roundTripOtherWay(iso, List.of())).isTrue();
  }

  @Test
  void stringToBooleanPrismLaws() {
    final Prism<String, Boolean> prism = StringOptics.stringToBoolean();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, "true")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "false")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "foo")).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, true)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, false)).isTrue();
  }

  @Test
  void stringToLongPrismLaws() {
    final Prism<String, Long> prism = StringOptics.stringToLong();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, "123")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "-42")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "0")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "01")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "+1")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "foo")).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 123L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, -42L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0L)).isTrue();
  }

  @Test
  void stringToIntegerPrismLaws() {
    final Prism<String, Integer> prism = StringOptics.stringToInteger();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, "123")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "-42")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "0")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "01")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "+1")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "foo")).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 123)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, -42)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0)).isTrue();
  }

  @Test
  void stringToBytePrismLaws() {
    final Prism<String, Byte> prism = StringOptics.stringToByte();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, "123")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "-42")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "0")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "01")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "+1")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "foo")).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (byte) 123)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (byte) -42)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (byte) 0)).isTrue();
  }

  @Test
  void stringToUriPrismLaws() {
    final Prism<String, URI> prism = StringOptics.stringToUri();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, "https://example.com")).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "invalid uri")).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, URI.create("https://example.com")))
        .isTrue();
  }

  @Test
  void stringToUuidPrismLaws() {
    final Prism<String, UUID> prism = StringOptics.stringToUuid();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, UUID.randomUUID().toString())).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, "invalid uuid")).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, UUID.randomUUID())).isTrue();
  }
}
