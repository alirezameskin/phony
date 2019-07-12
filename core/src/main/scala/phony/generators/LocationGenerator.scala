package phony.generators

import cats.Monad
import cats.implicits._
import phony.data.CountryData
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class LocationGenerator[F[_]: Monad: Locale: RandomUtility] {
  def latitude: F[String] =
    RandomUtility[F].double.map(d => ((d * 180) - 90).formatted("%.8g"))

  def longitude: F[String] =
    RandomUtility[F].double.map(d => ((d * 360) - 180).formatted("%.8g"))

  def countryName: F[String] =
    (Locale[F].location.map(_.countries) >>= RandomUtility[F].randomItem).map(_.name)

  def countryCode: F[String] =
    (Locale[F].location.map(_.countries) >>= RandomUtility[F].randomItem).map(_.code)

  def country: F[CountryData] =
    Locale[F].location.map(_.countries) >>= RandomUtility[F].randomItem

  def postalCode: F[String] =
    Locale[F].location.map(_.postcodeFormats)  >>= RandomUtility[F].randomItem
}
