package phony

import phony.data._

trait Locale[F[_]] {
  def name: F[NameData]
  def internet: F[InternetData]
  def calendar: F[CalendarData]
  def location: F[LocationData]
  def lorem: F[LoremData]
  def contact: F[ContactData]
}

object Locale {
  @inline def apply[F[_]](implicit instance: Locale[F]): Locale[F] = instance
}
