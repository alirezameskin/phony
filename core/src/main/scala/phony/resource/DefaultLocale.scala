package phony.resource

import java.io.InputStream

import cats.implicits._
import cats.{Functor, MonadError}
import io.circe.generic.auto._
import io.circe.parser._
import phony.Locale
import phony.data._

import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

class DefaultLocale[F[_]: Functor](val dataProvider: F[LocaleProvider]) extends Locale[F] {
  override def name: F[NameData] = dataProvider.map(_.names)

  override def internet: F[InternetData] = dataProvider.map(_.internet)

  override def calendar: F[CalendarData] = dataProvider.map(_.calendar)

  override def location: F[LocationData] = dataProvider.map(_.location)

  override def lorem: F[LoremData] = dataProvider.map(_.lorem)
}

object DefaultLocale {
  def apply[F[_]](language: String)(implicit ev: MonadError[F, Throwable]): DefaultLocale[F] = {

    val data: F[LocaleProvider] = (for {
      resource <- resource(language)
      content <- content(resource)
      data <- toJson(content)
    } yield data).fold(ev.raiseError, ev.pure)

    new DefaultLocale[F](data)
  }

  private def toJson(content: String) =
    decode[LocaleProvider](content).leftMap { _ =>
      new RuntimeException(s"Invalid Json")
    }

  private def content(stream: InputStream) =
    Try(scala.io.Source.fromInputStream(stream).getLines.mkString("\n")) match {
      case Success(value) => Right(value)
      case Failure(exception) => Left(exception)
    }

  private[phony] def resource(language: String): Either[Throwable, InputStream] =
    Try(resourceUrl(language).openStream) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(new RuntimeException(s"Invalid Resource for language ${language}"))
    }

  private[phony] def resourceUrl(language: String) =
    DefaultLocale.getClass.getResource(s"/locales/$language.json")
}
