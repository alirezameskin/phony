package alaki.generators

import java.text.SimpleDateFormat
import java.util.{Date, GregorianCalendar}

import alaki.Locale
import cats.Monad
import cats.implicits._

import scala.util.Random

class CalendarGenerator[F[_]: Monad](val random: Random)(implicit locale: Locale[F]) extends DataGenerator {
  def year: F[Int] =
    (random.nextInt(2019 - 1970) + 1970).pure[F]

  def day: F[String] =
    locale.calendar.map(_.days).map(randomItem)

  def month: F[String] =
    locale.calendar.map(_.months).map(randomItem)

  def time24h: F[String] =
    ("%02d".format(random.nextInt(24)) + ":" + "%02d".format(random.nextInt(60))).pure[F]

  def time12h: F[String] =
    ("%02d".format(random.nextInt(12)) + ":" + "%02d".format(random.nextInt(60))).pure[F]

  def date: F[Date] =
    for {
      y <- year
      m <- random.nextInt(12).pure[F]
      d <- random.nextInt(31).pure[F]
      h <- random.nextInt(24).pure[F]
      i <- random.nextInt(60).pure[F]
      s <- random.nextInt(60).pure[F]
    } yield new GregorianCalendar(y, m, d, h, i, s).getTime

  def date(format: String = "yyyy-MM-dd"): F[String] =
    date.map(d => new SimpleDateFormat(format).format(d))
}
