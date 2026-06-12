#!/usr/bin/env -S scala -cli shebang

case class Arity(arity: Int) {
  val isZero = arity == 0
  val fun: Seq[Int] = (0 to arity)


  val tts: Seq[String] = (0 until arity).map('A' + _).map(_.toChar).map(_.toString)
  val ts: Seq[Char] = (0 until arity).map('A' + _).map(_.toChar).mkString(", ")
}

def funTemp(i: Int) = {
  val arity = Arity(i)
  s"""|package org.kleinb.jfun;
      |
      |import java.util.Objects; ${if (arity.isZero) "\nimport java.util.function.Supplier;" else ""}
      |
      |/// $i, '${arity.ts}'
      |@FunctionalInterface
      |public interface Function$i<${arity.ts}${if (i > 0) ", " else ""}Z> ${if (arity.isZero) "extends Supplier<Z>" else ""} {
      |
      |  static <Z> Function$i<Z> constant(Z value) {
      |    return ${(0 until i).map(_ => "_").mkString("(", ", ", ")")} -> value;
      |  }
      |
      |  Z apply(${arity.tts.map(t => s"$t ${t.toLowerCase}").mkString(", ")});
      |
      |  @Override
      |  default Z get() {
      |    return apply();
      |  }
      |
      |  default Function$i<${arity.tts.reverse.mkString(", ")}${if (arity.isZero) "" else ", "}Z> reversed() {
      |    ${if (i <= 1) "return this" else s"(${arity.tts.reverse.map(t => s"$t ${t.toLowerCase}").mkString(", ")}) -> apply(${arity.tts.map(t => s"$t ${t.toLowerCase}").mkString(", ")})"};
      |  }
      |
      |  default Function$i<Z> curried() {
      |    return this;
      |  }
      |
      |  default <A> Function1<Tuple$i, Z> tupled() {
      |    return t -> {
      |      Objects.requireNonNull(t);
      |      return apply();
      |    };
      |  }
      |
      |  default <B> Function$i<B> andThen(Function1<? super Z, ? extends B> after) {
      |    Objects.requireNonNull(after);
      |    return () -> after.apply(apply());
      |  }
      |}
      |""".stripMargin
}

val inclusive: Seq[Int] = 0 to 3
val result = inclusive.map(funTemp).mkString("\n")
println(result)