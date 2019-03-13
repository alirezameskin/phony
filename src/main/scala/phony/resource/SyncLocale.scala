package phony.resource

import java.io.InputStream

import cats.effect.Sync
import cats.implicits._
import io.circe.generic.auto._
import io.circe.parser._
import phony.Locale
import phony.data._

import scala.language.higherKinds
import scala.util.Try

/**
 * A default instance for any `F[_]` with a `Sync` instance.
 */
class SyncLocale[F[_]](val dataProvider: F[LocaleProvider])(implicit A: Sync[F]) extends Locale[F] {
  override def name: F[NameData] = dataProvider.map(_.names)
  override def internet: F[InternetData] = dataProvider.map(_.internet)
  override def calendar: F[CalendarData] = dataProvider.map(_.calendar)
  override def location: F[LocationData] = dataProvider.map(_.location)
  override def lorem: F[LoremData] = dataProvider.map(_.lorem)
}

object SyncLocale {
  def apply[F[_] : Sync](language: String): SyncLocale[F] = {

    val fileContent: F[String] = Try {
      val resource = s"/locales/$language.json"
      val stream: InputStream = getClass.getResourceAsStream(resource)
      scala.io.Source.fromInputStream(stream).getLines.mkString("\n")
    }.liftTo[F]

    val jsonDecoder: String => F[LocaleProvider] = (content: String) =>
      decode[LocaleProvider](content).fold(Sync[F].raiseError, Sync[F].pure)

    new SyncLocale[F](fileContent >>= jsonDecoder)
  }
}
