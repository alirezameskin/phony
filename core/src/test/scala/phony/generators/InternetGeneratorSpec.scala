package phony.generators

import cats.implicits._
import org.scalatest.Matchers._
import org.scalatest.TryValues._
import org.scalatest._
import phony.data._
import phony.resource.{DefaultLocale, LocaleProvider}
import phony.{Locale, MonadRandomUtility}

import scala.util.Try

class InternetGeneratorSpec extends FunSuite {
  val dataProvider = LocaleProvider(
    LoremData(
      Vector("back", "background", "bad", "badly", "bag", "bake", "dolores", "et", "bind", "biological", "bird")
    ),
    NameData(
      Vector("John", "David", "George", "Ronald"),
      Vector("Smith", "Williams", "Johnson"),
      Vector.empty,
      Vector.empty
    ),
    InternetData(Vector("Yahoo.com", "gmail.com"), Vector(".co", ".com")),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[Try] = new DefaultLocale[Try](Try(dataProvider))
  implicit val random = new MonadRandomUtility[Try]()
  val generator = new InternetGenerator[Try]

  test("It should generate an Email") {
    generator.email.success.value should fullyMatch regex """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"""
  }

  test("It should generate password") {
    generator.password.success.value should have size 10
  }

  test("It should generate a valid UUID") {
    generator.uuid.map(_.toString).success.value should fullyMatch regex "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"
  }

  test("It should select a domain suffix from available domains") {
    val domain = generator.domain.success.value
    dataProvider.internet.domainSuffixes should contain (domain)
  }

  test("It should generate a valid hostname") {
    val hostname = generator.hostname.success.value
  }

  test("It should return https") {
    generator.protocol.success.value should fullyMatch regex "http|https"
  }

  test("It should generate a valid URL") {
    generator.url.success.value should fullyMatch regex "[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?"
  }

  test("It should generate a valid IP") {
    generator.ip.success.value should fullyMatch regex """((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])"""
  }

  test("It should generate a vaid IP v6") {
    generator.ipv6.success.value should fullyMatch regex """(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))"""
  }

  test("It should generate a hashtag") {
    generator.hashtag.success.value should fullyMatch regex """\B(\#[a-zA-Z]+\b)(?!;)"""
  }
}
