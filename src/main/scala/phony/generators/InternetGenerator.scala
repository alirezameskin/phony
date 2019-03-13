package phony.generators

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

import scala.language.higherKinds
import scala.util.Random

class InternetGenerator[F[_]: Monad](implicit val utility: RandomUtility[F], locale: Locale[F]) {
  private val IPV6Alphabet = "abcdefABCDEF0123456789".toList
  private val passwordAlphabet = "qwertyuiopasdfghjklmnbvcxzQWERTYUIOPASDFGHJKLMNBVCXZ123456789#-!_=%".toList

  def uuid: F[String] =
    java.util.UUID.randomUUID.toString.pure[F]

  def email: F[String] =
    for {
      name <- locale.name.map(_.firstNames) >>= utility.randomItem
      last <- locale.name.map(_.lastNames) >>= utility.randomItem
      domain <- locale.internet.map(_.emailDomains) >>= utility.randomItem
    } yield s"$name.$last@$domain".toLowerCase

  def password: F[String] =
    Random.shuffle(passwordAlphabet).take(10).mkString("").pure[F]

  def domain: F[String] =
    locale.internet.map(_.domainSuffixes) >>= utility.randomItem

  def hostname: F[String] =
    for {
      firstName <- locale.name.map(_.firstNames) >>= utility.randomItem
      domain <- locale.internet.map(_.domainSuffixes) >>= utility.randomItem
    } yield s"$firstName$domain".toLowerCase

  def protocol: F[String] =
    utility.randomItem(List("http", "https"))

  def url: F[String] =
    for {
      protocol <- protocol
      host <- hostname
    } yield s"$protocol://$host".toLowerCase

  def ip: F[String] = {
    val address: F[String] = (1 to 4).toList.foldLeftM(List.empty[Int]) { (items, _) =>
      utility.nextInt(255).map(part => items :+ part)
    }.map(_.mkString("."))

    address >>= {
      case "0.0.0.0" | "255.255.255.255" => ip
      case address => address.pure[F]
    }
  }

  def ipv6: F[String] =
    (1 to 8)
      .map(_ => Random.shuffle(IPV6Alphabet).take(4).mkString(""))
      .mkString(":")
      .pure[F]

  def hashtag: F[String] = locale.lorem.map(_.words) >>= { all =>
    for {
      size <- utility.nextInt(3)
      words <- utility.randomItems(size + 1)(all)
    } yield "#" + words.map(_.capitalize).mkString("")
  }
}
