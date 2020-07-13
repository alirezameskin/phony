package phony.cli

import cats.effect.{ExitCode, IO}
import cats.implicits._
import fs2.Stream
import phony.cats.effect.{SyncLocale, SyncRandomUtility}
import phony.cli.template.parser.{FunctionCall, TemplateAST, TemplateExpressionParser, Text}
import phony.cli.template.tokenizer.TemplateTokenizer
import phony.cli.template.{DefaultFunctionResolver, FunctionResolver}
import phony.{Phony, RandomUtility}

import scala.util.Try

object GenerateCommand {

  type Program = List[TemplateAST]

  def eval(token: TemplateAST, resolver: FunctionResolver[IO]) =
    token match {
      case Text(t) => IO(t)
      case op: FunctionCall =>
        if (resolver.resolve.isDefinedAt(op)) resolver.resolve.apply(op)
        else IO.raiseError(new Exception(s"Invalid Function ${op.func}(" + op.args.mkString(", ") + ")"))
    }

  def execute(program: Program, resolver: FunctionResolver[IO]): IO[String] =
    program.traverse(a => eval(a, resolver)).map(_.mkString(""))

  def parse(template: String): IO[Program] =
    IO.fromEither(for {
      tokens <- TemplateTokenizer(template)
      ast    <- TemplateExpressionParser(tokens)
    } yield ast)

  def functionResolver(language: String): IO[FunctionResolver[IO]] = {
    implicit val locale                     = SyncLocale[IO](language)
    implicit def utility: RandomUtility[IO] = new SyncRandomUtility[IO]()
    val P                                   = new Phony[IO]

    IO(new DefaultFunctionResolver(P))
  }

  def generator(tpl: String, language: String): Stream[IO, String] =
    Stream
      .eval(parse(tpl))
      .flatMap(prg =>
        Stream
          .eval(functionResolver(language))
          .flatMap(resolver => Stream.repeatEval(execute(prg, resolver)))
      )

  def execute(configs: GenerateOptions): IO[ExitCode] = {
    val template = configs.templateFile match {
      case Some(path) =>
        IO.fromTry(Try(scala.io.Source.fromFile(path.toFile).mkString("")))
      case None =>
        IO(configs.template.getOrElse("{{alphanumeric.number()}}"))
    }

    val result = for {
      tpl <- template
      res <- generator(tpl, configs.language).map(println).take(configs.count).compile.drain
    } yield res

    result.attempt.flatMap {
      case Right(_)  => IO(ExitCode.Success)
      case Left(err) => IO(println(fansi.Color.Red(err.getMessage))) *> IO(ExitCode.Error)
    }
  }
}
