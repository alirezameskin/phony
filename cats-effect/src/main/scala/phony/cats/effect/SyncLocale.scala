package phony.cats.effect

import java.io.InputStream

import _root_.io.circe.generic.auto._
import _root_.io.circe.parser._
import cats.Monad
import cats.effect.Sync
import phony.Locale
import phony.resource.{DefaultLocale, LocaleProvider}

import scala.language.higherKinds

object SyncLocale {
  def apply[F[_]: Sync: Monad](language: String): Locale[F] = load[F](language)

  private def load[F[_]: Sync](language: String): Locale[F] = {
    val resource: F[InputStream] = Sync[F].delay(DefaultLocale.resourceUrl(language).openStream)
    val content: F[String] = Sync[F].flatMap(resource)(read[F])
    val data: F[LocaleProvider] = Sync[F].flatMap(content)(toJson[F])

    new DefaultLocale[F](data)
  }

  private def read[F[_]: Sync](stream: InputStream): F[String] =
    Sync[F].delay(scala.io.Source.fromInputStream(stream).getLines.mkString("\n"))

  private def toJson[F[_]: Sync](content: String): F[LocaleProvider] =
    decode[LocaleProvider](content).fold(Sync[F].raiseError, Sync[F].pure)

}
