package phony.generators

import java.text.SimpleDateFormat
import java.util.{Date, GregorianCalendar}

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class CalendarGenerator[F[_]: Monad](implicit val utility: RandomUtility[F], locale: Locale[F]) {
  def year: F[Int] =
    utility.nextInt(2019 - 1970).map(_ + 1970)

  def day: F[String] =
    locale.calendar.map(_.days).flatMap(utility.randomItem)

  def month: F[String] =
    locale.calendar.map(_.months).flatMap(utility.randomItem)

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
}
