package phony.generators

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class ContactGenerator[F[_]: Monad: Locale: RandomUtility] {
  def firstName: F[String] =
    Locale[F].name.map(_.firstNames) >>= RandomUtility[F].randomItem

  def lastName: F[String] =
    Locale[F].name.map(_.lastNames) >>= RandomUtility[F].randomItem

  def prefix: F[String] =
    Locale[F].name.map(_.prefixes) >>= RandomUtility[F].randomItem

  def suffix: F[String] =
    Locale[F].name.map(_.suffixes) >>= RandomUtility[F].randomItem

  def fullName: F[String] =
    fullName(false, false)

  def fullName(withPrefix: Boolean = false, withSuffix: Boolean = false): F[String] =
    (withPrefix, withSuffix) match {
      case (true, true) => (prefix, firstName, lastName, suffix).mapN(combine4(" "))
      case (true, false) => (prefix, firstName, lastName).mapN(combine3(" "))
      case (false, true) => (firstName, lastName, suffix).mapN(combine3(" "))
      case (false, false) => (firstName, lastName).mapN(combine2(" "))
    }

  def username: F[String] =
    for {
      first <- Locale[F].name.map(_.firstNames) >>= RandomUtility[F].randomItem
      last <- Locale[F].name.map(_.lastNames) >>= RandomUtility[F].randomItem
      rand <- RandomUtility[F].int(1000)
    } yield s"${first}_${last}_${rand}".toLowerCase

  def phone: F[String] =
    for {
      contact <- Locale[F].contact
      format <- RandomUtility[F].randomItem(contact.phoneNumberFormats)
      result <- RandomUtility[F].numerify(format)
    } yield result

  def cellPhone: F[String] =
    for {
      contact <- Locale[F].contact
      format <- RandomUtility[F].randomItem(contact.cellPhoneFormats)
      result <- RandomUtility[F].numerify(format)
    } yield result

  private def combine4(glue: String)(p1: String, p2: String, p3: String, p4: String): String =
    List(p1, p2, p3, p4).mkString(glue).trim

  private def combine3(glue: String)(p1: String, p2: String, p3: String): String =
    List(p1, p2, p3).mkString(glue).trim

  private def combine2(glue: String)(p1: String, p2: String): String =
    List(p1, p2).mkString(glue).trim
}
