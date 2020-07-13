package phony.cli

import java.nio.file.Path

import cats.effect._
import cats.implicits._
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp

case class GenerateOptions(language: String, template: Option[String], templateFile: Option[Path], count: Long)

object PhonyApp extends CommandIOApp(name = "phony", header = "Random data generator", version = "0.4.0") {
  val languageOpts: Opts[String] =
    Opts.option[String]("language", "Language.", short = "l").orElse(Opts("en"))

  val templateOpts: Opts[Option[String]] =
    Opts.option[String]("template", "Template", short = "t").orNone

  val templateFileOpts: Opts[Option[Path]] =
    Opts.option[Path]("templateFile", "Path to the template file.", short = "f").orNone

  val countOpts: Opts[Long] =
    Opts.option[Long]("count", "Iteration", "c").orElse(Opts(1L))

  val pathOpts: Opts[String] =
    Opts.argument[String](metavar = "path")

  val generateOpts: Opts[GenerateOptions] =
    (languageOpts, templateOpts, templateFileOpts, countOpts).mapN(GenerateOptions)

  override def main: Opts[IO[ExitCode]] = generateOpts.map { options =>
    GenerateCommand.execute(options)
  }
}
