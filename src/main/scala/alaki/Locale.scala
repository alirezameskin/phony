package alaki

import alaki.data._
import alaki.resource.SyncLocale
import cats.effect.IO

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
