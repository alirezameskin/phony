package phony.generators

import java.text.SimpleDateFormat
import java.util.{Date, GregorianCalendar}

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class CalendarGenerator[F[_]: Monad](val utility: RandomUtility)(implicit locale: Locale[F]) {
  def year: F[Int] =
    (utility.nextInt(2019 - 1970) + 1970).pure[F]

  def day: F[String] =
    locale.calendar.map(_.days).map(utility.randomItem)

  def month: F[String] =
    locale.calendar.map(_.months).map(utility.randomItem)

  def time24h: F[String] =
    ("%02d".format(utility.nextInt(24)) + ":" + "%02d".format(utility.nextInt(60))).pure[F]

  def time12h: F[String] =
    ("%02d".format(utility.nextInt(12)) + ":" + "%02d".format(utility.nextInt(60))).pure[F]

  def date: F[Date] =
    for {
      y <- year
      m <- utility.nextInt(12).pure[F]
      d <- utility.nextInt(31).pure[F]
      h <- utility.nextInt(24).pure[F]
      i <- utility.nextInt(60).pure[F]
      s <- utility.nextInt(60).pure[F]
    } yield new GregorianCalendar(y, m, d, h, i, s).getTime

  def date(format: String = "yyyy-MM-dd"): F[String] =
    date.map(d => new SimpleDateFormat(format).format(d))
}
