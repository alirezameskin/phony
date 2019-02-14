package alaki.generators

import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import alaki.{Locale, RandomUtility}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

class NameGeneratorSpec extends FunSuite with MockFactory {

  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector("John"), Vector("Richard"), Vector("Mr"), Vector("I")),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val random = mock[RandomUtility]
  val generator = new NameGenerator[IO](random)

  test("It should generate firstName") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("John")
    generator.firstName.map(name => assert(name == "John")).unsafeRunSync()
  }

  test("It should generate lastName") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning("Richard")
    generator.lastName.map(name => assert(name == "Richard")).unsafeRunSync()
  }

  test("It should generate prefix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.prefixes).returning("Mr")
    generator.prefix.map(prefix => assert(prefix == "Mr")).unsafeRunSync()
  }

  test("It should generate suffix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.suffixes).returning("I")
    generator.suffix.map(suffix => assert(suffix == "I")).unsafeRunSync()
  }

  test("It should generate full name") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("John")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning("Richard")
    generator.fullName.map(name => assert(name == "John Richard")).unsafeRunSync()
  }

  test("It should generate full name with prefix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.prefixes).returning("Mr")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("John")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning("Richard")
    generator.fullName(true, false).map(name => assert(name == "Mr John Richard")).unsafeRunSync()
  }

  test("It should generate full name with suffix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("John")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning("Richard")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.suffixes).returning("I")
    generator.fullName(false, true).map(name => assert(name == "John Richard I")).unsafeRunSync()
  }

  test("It should generate full name with prefix and suffix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.prefixes).returning("Mr")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("John")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning("Richard")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.suffixes).returning("I")
    generator.fullName(true, true).map(lastName => assert(lastName == "Mr John Richard I")).unsafeRunSync()
  }

  test("It should generate username") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning("John")
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning("Richard")
    (random.nextInt(_: Int)).expects(1000).returning(45)

    generator.username.map(username => assert(username == "john_richard_45")).unsafeRunSync()
  }
}
