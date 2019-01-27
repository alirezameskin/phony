package alaki.generators

import alaki.Locale
import cats.Monad
import cats.implicits._

import scala.util.Random

class InternetGenerator[F[_]: Monad](val random: Random)(implicit locale: Locale[F]) extends DataGenerator {
  private val IPV6Alphabet = "abcdefABCDEF0123456789".toList
  private val passwordAlphabet = "qwertyuiopasdfghjklmnbvcxzQWERTYUIOPASDFGHJKLMNBVCXZ123456789#-!_=%".toList

  def uuid: F[String] =
    java.util.UUID.randomUUID.toString.pure[F]

  def email: F[String] =
    for {
      name <- locale.name.map(_.firstNames).map(randomItem)
      last <- locale.name.map(_.lastNames).map(randomItem)
      domain <- locale.internet.map(_.emailDomains).map(randomItem)
    } yield s"$name.$last@$domain".toLowerCase

  def password: F[String] =
    Random.shuffle(passwordAlphabet).take(10).mkString("").pure[F]

  def domain: F[String] =
    locale.internet.map(_.domainSuffixes).map(randomItem)

  def hostname: F[String] =
    for {
      firstName <- locale.name.map(_.firstNames).map(randomItem)
      domain <- locale.internet.map(_.domainSuffixes).map(randomItem)
    } yield s"$firstName$domain".toLowerCase

  def protocol: F[String] =
    randomItem(List("http", "https")).pure[F]

  def url: F[String] =
    for {
      protocol <- protocol
      host <- hostname
    } yield s"$protocol://$host".toLowerCase

  def ip: F[String] =
    (1 to 4).map(_ => random.nextInt(255)).mkString(".") match {
      case "0.0.0.0" | "255.255.255.255" => ip
      case address => address.pure[F]
    }

  def ipv6: F[String] =
    (1 to 8)
      .map(_ => Random.shuffle(IPV6Alphabet).take(4).mkString(""))
      .mkString(":")
      .pure[F]
}
