package phony

import java.util.UUID

import phony.data._

import scala.language.higherKinds

abstract class RandomUtility[F[_]: Locale] {
  def name: F[NameData] = Locale[F].name

  def internet: F[InternetData] = Locale[F].internet

  def calendar: F[CalendarData] = Locale[F].calendar

  def location: F[LocationData] = Locale[F].location

  def lorem: F[LoremData] = Locale[F].lorem

  def boolean: F[Boolean]

  def double: F[Double]

  def float: F[Float]

  def char: F[Char]

  def int: F[Int]

  def int(max: Int): F[Int]

  def long: F[Long]

  def uuid: F[UUID]

  def randomItem[A](items: Seq[A]): F[A]

  def randomItems[A](count: Int)(items: Seq[A]): F[List[A]]

  def numerify(format: String): F[String]

  def letterify(format: String): F[String]

  def bothify(format: String): F[String]
}

object RandomUtility {
  def apply[F[_]: Locale](implicit instance: RandomUtility[F]): RandomUtility[F] = instance
}
