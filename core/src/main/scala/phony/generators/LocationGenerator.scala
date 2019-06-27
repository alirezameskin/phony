package phony.generators

import cats.Monad
import cats.implicits._
import phony.data.Country
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class LocationGenerator[F[_]: Monad](implicit val utility: RandomUtility[F]) {
  def latitude: F[String] =
    utility.double.map(d => ((d * 180) - 90).formatted("%.8g"))

  def longitude: F[String] =
    utility.double.map(d => ((d * 360) - 180).formatted("%.8g"))

  def countryName: F[String] =
    (utility.location.map(_.countries) >>= utility.randomItem).map(_.name)

  def countryCode: F[String] =
    (utility.location.map(_.countries) >>= utility.randomItem).map(_.code)

  def country: F[Country] =
    utility.location.map(_.countries) >>= utility.randomItem

}
