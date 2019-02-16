package phony.generators

import cats.Monad
import cats.implicits._
import phony.data.Country
import phony.{Locale, RandomUtility}

class LocationGenerator[F[_]: Monad](val utility: RandomUtility)(implicit locale: Locale[F]) {
  def latitude: F[String] =
    ((utility.nextDouble * 180) - 90).formatted("%.8g").pure[F]

  def longitude: F[String] =
    ((utility.nextDouble * 360) - 180).formatted("%.8g").pure[F]

  def countryName: F[String] =
    locale.location.map(_.countries).map(utility.randomItem).map(_.name)

  def countryCode: F[String] =
    locale.location.map(_.countries).map(utility.randomItem).map(_.code)

  def country: F[Country] =
    locale.location.map(_.countries).map(utility.randomItem)

}
