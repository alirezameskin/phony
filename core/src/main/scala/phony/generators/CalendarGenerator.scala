package phony.generators

import java.text.SimpleDateFormat
import java.util.{Date, GregorianCalendar, TimeZone}

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

class CalendarGenerator[F[_]: Monad: Locale: RandomUtility] {
  def year: F[Int] =
    RandomUtility[F].int(1970, 2025)

  def day: F[String] =
    Locale[F].calendar.map(_.days) >>= RandomUtility[F].randomItem

  def month: F[String] =
    Locale[F].calendar.map(_.months) >>= RandomUtility[F].randomItem

  def time24h: F[String] =
    for {
      hour <- RandomUtility[F].int(24)
      minute <- RandomUtility[F].int(60)
    } yield "%02d".format(hour) + ":" + "%02d".format(minute)

  def time12h: F[String] =
    for {
      hour <- RandomUtility[F].int(12)
      minute <- RandomUtility[F].int(60)
    } yield "%02d".format(hour) + ":" + "%02d".format(minute)

  def unixTime: F[Long] =
    date.map(_.getTime)

  def date: F[Date] =
    for {
      y <- year
      m <- RandomUtility[F].int(12)
      d <- RandomUtility[F].int(31)
      h <- RandomUtility[F].int(24)
      i <- RandomUtility[F].int(60)
      s <- RandomUtility[F].int(60)
    } yield new GregorianCalendar(y, m, d, h, i, s).getTime

  def date(format: String = "yyyy-MM-dd"): F[String] =
    date.map(d => new SimpleDateFormat(format).format(d))

  def timezone: F[String] =
    RandomUtility[F].randomItem(TimeZone.getAvailableIDs.toList)

  def iso8601: F[String] =
    for {
      zone <- timezone
      d <- date
    } yield {
      val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
      formatter.setTimeZone(TimeZone.getTimeZone(zone))

      formatter.format(d)
    }
}
