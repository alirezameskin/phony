package phony.generators

import phony.data._
import phony.resource.{LocaleProvider, SyncLocale}
import phony.{Locale, RandomUtility}
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
  implicit val random = mock[RandomUtility[IO]]
  val generator = new NameGenerator[IO]

  test("It should generate firstName") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("John"))
    generator.firstName.map(name => assert(name == "John")).unsafeRunSync()
  }

  test("It should generate lastName") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning(IO("Richard"))
    generator.lastName.map(name => assert(name == "Richard")).unsafeRunSync()
  }

  test("It should generate prefix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.prefixes).returning(IO("Mr"))
    generator.prefix.map(prefix => assert(prefix == "Mr")).unsafeRunSync()
  }

  test("It should generate suffix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.suffixes).returning(IO("I"))
    generator.suffix.map(suffix => assert(suffix == "I")).unsafeRunSync()
  }

  test("It should generate full name") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("John"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning(IO("Richard"))
    generator.fullName.map(name => assert(name == "John Richard")).unsafeRunSync()
  }

  test("It should generate full name with prefix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.prefixes).returning(IO("Mr"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("John"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning(IO("Richard"))
    generator.fullName(true, false).map(name => assert(name == "Mr John Richard")).unsafeRunSync()
  }

  test("It should generate full name with suffix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("John"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning(IO("Richard"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.suffixes).returning(IO("I"))
    generator.fullName(false, true).map(name => assert(name == "John Richard I")).unsafeRunSync()
  }

  test("It should generate full name with prefix and suffix") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.prefixes).returning(IO("Mr"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("John"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning(IO("Richard"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.suffixes).returning(IO("I"))
    generator.fullName(true, true).map(lastName => assert(lastName == "Mr John Richard I")).unsafeRunSync()
  }

  test("It should generate username") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.firstNames).returning(IO("John"))
    (random.randomItem(_: Seq[String])).expects(dataProvider.names.lastNames).returning(IO("Richard"))
    (random.nextInt(_: Int)).expects(1000).returning(IO(45))

    generator.username.map(username => assert(username == "john_richard_45")).unsafeRunSync()
  }
}
