package phony.cli

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import fs2.Stream
import phony.cats.effect.{SyncLocale, SyncRandomUtility}
import phony.{Phony, RandomUtility}
import phony.cli.template.parser.{FunctionCall, TemplateAST, TemplateExpressionParser, Text}
import phony.cli.template.tokenizer.TemplateTokenizer
import phony.cli.template.{DefaultFunctionResolver, FunctionResolver}

object PhonyApp extends IOApp {

  type Program = List[TemplateAST]

  def eval(token: TemplateAST, resolver: FunctionResolver[IO]) =
    token match {
      case Text(t) => IO(t)
      case op: FunctionCall =>
        if (resolver.resolve.isDefinedAt(op)) resolver.resolve.apply(op)
        else IO.raiseError(new Exception(s"Invalid Function ${op.func}"))
    }

  def execute(program: Program, resolver: FunctionResolver[IO]): IO[String] =
    program.traverse(a => eval(a, resolver)).map(_.mkString(""))

  def parse(template: String): IO[Program] =
    IO.fromEither(for {
      tokens <- TemplateTokenizer(template)
      ast    <- TemplateExpressionParser(tokens)
    } yield ast)

  def functionResolver(language: String): IO[FunctionResolver[IO]] = {
    implicit val locale = SyncLocale[IO](language)
    implicit def utility: RandomUtility[IO] = new SyncRandomUtility[IO]()
    val P               = new Phony[IO]

    IO(new DefaultFunctionResolver(P))
  }

  def generator(tpl: String, language: String): Stream[IO, String] =
    Stream.eval(parse(tpl))
      .flatMap(prg =>
        Stream.eval(functionResolver(language))
          .flatMap(resolver => Stream.repeatEval(execute(prg, resolver)))
      )

  override def run(args: List[String]): IO[ExitCode] = {
    println(args)
    val count = 100L
    val language = "en"
    val tpl =
      """
        |INSERT INTO users (first_name, last_name, age) VALUES("{{ contact.firstName }}", "{{ contact.lastName }}", {{alphanumeric.number(18, 68)}});
        |""".stripMargin.trim

    generator(tpl, language).map(println).take(count).compile.drain.as(ExitCode.Success)
  }
}
