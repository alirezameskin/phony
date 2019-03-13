package phony.generators

import phony.data._
import phony.resource.{LocaleProvider, SyncLocale}
import phony.{Locale, RandomUtility}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.util.Random

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
  implicit val random = mock[RandomUtility[IO]]
  val generator = new InternetGenerator[IO]

  test("It should generate an Email") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("John"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning(IO("Smith"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.emailDomains).returning(IO("Yahoo.com"))
    generator.email.map(email => assert(email == "john.smith@yahoo.com")).unsafeRunSync
  }

  test("It should generate password") {
    val pass = Random.shuffle(generator.passwordAlphabet).take(10)
    (random.randomItems(_:Int)(_:Seq[Char])).expects(10, generator.passwordAlphabet).returning(IO(pass))
    generator.password.map(pass => assert(pass.size == 10)).unsafeRunSync
  }

  test("It should generate a valid UUID") {
    val uuid = java.util.UUID.randomUUID()
    (random.nextUUID _).expects().returning(IO(uuid))
    generator.uuid
      .map(
        guuid => assert(guuid == uuid.toString)
      )
      .unsafeRunSync
  }

  test("It should select a domain suffix from available domains") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.domainSuffixes).returning(IO(".com"))

    generator.domain
      .map(domain => assert(domain == ".com"))
      .unsafeRunSync
  }

  test("It should generate a valid hostname") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("George"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.domainSuffixes).returning(IO(".com"))
    generator.hostname.map(host => assert(host == "george.com")).unsafeRunSync
  }

  test("It should return https") {
    (random.randomItem(_: Seq[String])).expects(List("http", "https")).returning(IO("https"))
    generator.protocol.map(protocol => assert(protocol == "https")).unsafeRunSync
  }

  test("It should generate a valid URL") {
    (random.randomItem(_: Seq[String])).expects(List("http", "https")).returning(IO("https"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("Ronald"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.internet.domainSuffixes).returning(IO(".co"))

    generator.url.map(url => assert(url == "https://ronald.co")).unsafeRunSync
  }

  test("It should generate a valid IP") {
    (random.nextInt(_: Int)).expects(255).returning(IO(127))
    (random.nextInt(_: Int)).expects(255).returning(IO(0))
    (random.nextInt(_: Int)).expects(255).returning(IO(0))
    (random.nextInt(_: Int)).expects(255).returning(IO(1))

    generator.ip.map(ip => assert(ip == "127.0.0.1")).unsafeRunSync
  }

  test("It should not return an invalid IP like 0.0.0.0") {
    (random.nextInt(_: Int)).expects(255).returning(IO(0))
    (random.nextInt(_: Int)).expects(255).returning(IO(0))
    (random.nextInt(_: Int)).expects(255).returning(IO(0))
    (random.nextInt(_: Int)).expects(255).returning(IO(0))

    (random.nextInt(_: Int)).expects(255).returning(IO(135))
    (random.nextInt(_: Int)).expects(255).returning(IO(125))
    (random.nextInt(_: Int)).expects(255).returning(IO(120))
    (random.nextInt(_: Int)).expects(255).returning(IO(110))

    generator.ip.map(ip => assert(ip == "135.125.120.110")).unsafeRunSync
  }

  test("It should generate a vaid IP v6") {
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('a', '5', '3', 'A')))
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('b', '4', '1', 'A')))
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('a', '5', '3', 'B')))
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('b', '5', 'c', 'F')))
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('a', '8', '3', 'D')))
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('b', '6', 'b', 'C')))
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('9', '5', 'a', 'e')))
    (random.randomItems(_:Int)(_:Seq[Char])).expects(4, generator.IPV6Alphabet).returning(IO(List('6', '5', '3', '8')))

    generator.ipv6
      .map(
        ip => assert(ip == "6538:95ae:b6bC:a83D:b5cF:a53B:b41A:a53A")
      )
      .unsafeRunSync
  }

  test("It should generate a hashtag") {
    (random.nextInt(_: Int)).expects(3).returning(IO(1))
    (random.randomItems(_: Int)(_: Seq[String])).expects(2, dataProvider.lorem.words).returning(IO(List("bake", "dolores")))
    generator.hashtag.map(hashtag => assert(hashtag == "#BakeDolores")).unsafeRunSync
  }
}
