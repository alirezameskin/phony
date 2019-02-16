package phony

import cats.effect.IO
import phony.data._
import phony.resource.SyncLocale

trait Locale[F[_]] {
  def name: F[NameData]
  def internet: F[InternetData]
  def calendar: F[CalendarData]
  def location: F[LocationData]
  def lorem: F[LoremData]
}

object Locale {
  lazy val ENGLISH = SyncLocale[IO]("en")
}
