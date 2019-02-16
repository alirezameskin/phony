package phony.generators

import phony.data._
import phony.resource.{LocaleProvider, SyncLocale}
import phony.{Locale, RandomUtility}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

class InternetGeneratorSpec extends FunSuite with MockFactory {
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

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val random = mock[RandomUtility]
  val generator = new InternetGenerator[IO](random)

  test("It should generate an Email") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("John")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning("Smith")
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.emailDomains).returning("Yahoo.com")
    generator.email.map(email => assert(email == "john.smith@yahoo.com")).unsafeRunSync
  }

  test("It should generate password") {
    generator.password.map(pass => assert(pass.size == 10)).unsafeRunSync
  }

  test("It should generate a valid UUID") {
    generator.uuid
      .map(
        uuid => assert(uuid.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"))
      )
      .unsafeRunSync
  }

  test("It should select a domain suffix from available domains") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.domainSuffixes).returning(".com")

    generator.domain
      .map(domain => assert(domain == ".com"))
      .unsafeRunSync
  }

  test("It should generate a valid hostname") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("George")
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.domainSuffixes).returning(".com")
    generator.hostname.map(host => assert(host == "george.com")).unsafeRunSync
  }

  test("It should return https") {
    (random.randomItem(_: Seq[String])).expects(List("http", "https")).returning("https")
    generator.protocol.map(protocol => assert(protocol == "https")).unsafeRunSync
  }

  test("It should generate a valid URL") {
    (random.randomItem(_: Seq[String])).expects(List("http", "https")).returning("https")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("Ronald")
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.domainSuffixes).returning(".co")

    generator.url.map(url => assert(url == "https://ronald.co")).unsafeRunSync
  }

  test("It should generate a valid IP") {
    (random.nextInt(_: Int)).expects(255).returning(127)
    (random.nextInt(_: Int)).expects(255).returning(0)
    (random.nextInt(_: Int)).expects(255).returning(0)
    (random.nextInt(_: Int)).expects(255).returning(1)

    generator.ip.map(ip => assert(ip == "127.0.0.1")).unsafeRunSync
  }

  test("It should not return an invalid IP like 0.0.0.0") {
    (random.nextInt(_: Int)).expects(255).returning(0)
    (random.nextInt(_: Int)).expects(255).returning(0)
    (random.nextInt(_: Int)).expects(255).returning(0)
    (random.nextInt(_: Int)).expects(255).returning(0)

    (random.nextInt(_: Int)).expects(255).returning(135)
    (random.nextInt(_: Int)).expects(255).returning(125)
    (random.nextInt(_: Int)).expects(255).returning(120)
    (random.nextInt(_: Int)).expects(255).returning(110)

    generator.ip.map(ip => assert(ip == "135.125.120.110")).unsafeRunSync
  }

  test("It should generate a vaid IP v6") {
    generator.ipv6
      .map(
        ip => assert(ip.matches("(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$"))
      )
      .unsafeRunSync
  }

  test("It should generate a hashtag") {
    (random.nextInt(_: Int)).expects(3).returning(1)
    (random.randomItems(_: Int)(_: Seq[String])).expects(2, dataProvider.lorem.words).returning(List("bake", "dolores"))
    generator.hashtag.map(hashtag => assert(hashtag == "#BakeDolores")).unsafeRunSync
  }
}
