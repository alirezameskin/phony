package phony.generators

import java.text.SimpleDateFormat
import java.util.{Date, GregorianCalendar, TimeZone}

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class CalendarGenerator[F[_]: Monad](implicit val utility: RandomUtility[F], locale: Locale[F]) {
  def year: F[Int] =
    utility.nextInt(2019 - 1970).map(_ + 1970)

  def day: F[String] =
    locale.calendar.map(_.days) >>= utility.randomItem

  def month: F[String] =
    locale.calendar.map(_.months) >>= utility.randomItem

  def time24h: F[String] =
    for {
      hour <- utility.nextInt(24)
      minute <- utility.nextInt(60)
    } yield "%02d".format(hour) + ":" + "%02d".format(minute)

  def time12h: F[String] =
    for {
      hour <- utility.nextInt(12)
      minute <- utility.nextInt(60)
    } yield "%02d".format(hour) + ":" + "%02d".format(minute)

  def unixTime:F[Long] =
    date.map(_.getTime)

  def date: F[Date] =
    for {
      y <- year
      m <- utility.nextInt(12)
      d <- utility.nextInt(31)
      h <- utility.nextInt(24)
      i <- utility.nextInt(60)
      s <- utility.nextInt(60)
    } yield new GregorianCalendar(y, m, d, h, i, s).getTime

  def date(format: String = "yyyy-MM-dd"): F[String] =
    date.map(d => new SimpleDateFormat(format).format(d))

  def timezone: F[String] =
    utility.randomItem(TimeZone.getAvailableIDs.toList)

  def iso8601: F[String] = for {
    zone <- timezone
    d <- date
  } yield {
    val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    formatter.setTimeZone(TimeZone.getTimeZone(zone))

    formatter.format(d)
  }
}
