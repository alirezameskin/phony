package alaki.generators

import alaki.Locale
import alaki.data.Country
import cats.Monad
import cats.implicits._

import scala.util.Random

class LocationGenerator[F[_]: Monad](val random: Random)(implicit locale: Locale[F]) extends DataGenerator {
  def latitude: F[String] =
    ((random.nextDouble * 180) - 90).formatted("%.8g").pure[F]

  def longitude: F[String] =
    ((random.nextDouble * 360) - 180).formatted("%.8g").pure[F]

  def countryName: F[String] =
    locale.location.map(_.countries).map(randomItem).map(_.name)

  def countryCode: F[String] =
    locale.location.map(_.countries).map(randomItem).map(_.code)

  def country: F[Country] =
    locale.location.map(_.countries).map(randomItem)

}
