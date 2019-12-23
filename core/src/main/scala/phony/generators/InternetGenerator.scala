package phony.generators

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

class InternetGenerator[F[_]: Monad: RandomUtility: Locale] {
  private val utility = RandomUtility[F]
  private[phony] val IPV6Alphabet = "abcdefABCDEF0123456789".toList
  private[phony] val passwordAlphabet = "qwertyuiopasdfghjklmnbvcxzQWERTYUIOPASDFGHJKLMNBVCXZ123456789#-!_=%".toList

  def uuid: F[String] =
    utility.uuid.map(_.toString)

  def email: F[String] =
    for {
      name <- Locale[F].name.map(_.firstNames) >>= utility.randomItem
      last <- Locale[F].name.map(_.lastNames) >>= utility.randomItem
      domain <- Locale[F].internet.map(_.emailDomains) >>= utility.randomItem
    } yield s"$name.$last@$domain".toLowerCase

  def password: F[String] =
    utility.randomItems(10)(passwordAlphabet).map(_.mkString(""))

  def domain: F[String] =
    Locale[F].internet.map(_.domainSuffixes) >>= utility.randomItem

  def hostname: F[String] =
    for {
      firstName <- Locale[F].name.map(_.firstNames) >>= utility.randomItem
      domain <- Locale[F].internet.map(_.domainSuffixes) >>= utility.randomItem
    } yield s"$firstName$domain".toLowerCase

  def protocol: F[String] =
    utility.randomItem(List("http", "https"))

  def url: F[String] =
    for {
      protocol <- protocol
      host <- hostname
    } yield s"$protocol://$host".toLowerCase

  def ip: F[String] = {
    val address: F[String] = (1 to 4).toList
      .foldLeftM(List.empty[Int]) { (items, _) =>
        utility.int(255).map(part => items :+ part)
      }
      .map(_.mkString("."))

    address >>= {
      case "0.0.0.0" | "255.255.255.255" => ip
      case address => address.pure[F]
    }
  }

  def ipv6: F[String] =
    (1 to 8).toList
      .foldLeftM(List.empty[String]) { (list: List[String], _) =>
        utility.randomItems(4)(IPV6Alphabet).map(_.mkString("")).map(item => item +: list)
      }
      .map(_.mkString(":"))

  def hashtag: F[String] = Locale[F].lorem.map(_.words) >>= { all =>
    for {
      size <- utility.int(3)
      words <- utility.randomItems(size + 1)(all)
    } yield "#" + words.map(_.capitalize).mkString("")
  }
}
