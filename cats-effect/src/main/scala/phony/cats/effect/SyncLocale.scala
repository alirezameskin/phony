package phony.cats.effect

import java.io.InputStream

import _root_.io.circe.generic.auto._
import _root_.io.circe.parser._
import cats.Monad
import cats.effect.Sync
import cats.implicits._
import phony.Locale
import phony.resource.{DefaultLocale, LocaleProvider}

object SyncLocale {
  def apply[F[_]: Sync: Monad](language: String): Locale[F] = load[F](language)

  private def load[F[_]: Sync](language: String): Locale[F] = {
    val data = for {
      resource <- resource[F](language)
      content  <- read[F](resource)
      data     <- toJson[F](content)
    } yield data

    new DefaultLocale[F](data)
  }

  private def resource[F[_]: Sync](language: String): F[InputStream] =
    Sync[F].delay(DefaultLocale.resourceUrl(language)).flatMap {
      case null => Sync[F].raiseError[InputStream](new RuntimeException(s"Not Supported language ${language}"))
      case url  => Sync[F].pure(url.openStream)
    }

  private def read[F[_]: Sync](stream: InputStream): F[String] =
    Sync[F].delay(scala.io.Source.fromInputStream(stream).getLines.mkString("\n"))

  private def toJson[F[_]: Sync](content: String): F[LocaleProvider] =
    decode[LocaleProvider](content).fold(Sync[F].raiseError, Sync[F].pure)

}
