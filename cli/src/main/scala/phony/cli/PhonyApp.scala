package phony.cli

import cats.effect.{ExitCode, IO, IOApp}

object PhonyApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = IO(println("Hello World")).map(_ => ExitCode.Success)
}
