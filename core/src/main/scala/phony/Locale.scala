package phony

import phony.data._

import scala.language.higherKinds

trait Locale[F[_]] {
  def name: F[NameData]
  def internet: F[InternetData]
  def calendar: F[CalendarData]
  def location: F[LocationData]
  def lorem: F[LoremData]
}

object Locale {
  def apply[F[_]](implicit instance: Locale[F]): Locale[F] = instance
}