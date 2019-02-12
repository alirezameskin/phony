package alaki.generators

import alaki.Locale
import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.util.Random

class NameGeneratorSpec extends FunSuite with MockFactory {

  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector("John"), Vector("Richard"), Vector("Mr"), Vector("I")),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val random = mock[Random]
  val generator = new NameGenerator[IO](random)

  test("It should generate firstName") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    val test = for (firstName <- generator.firstName) yield assert(firstName == "John")
    test.unsafeRunSync()
  }

  test("It should generate lastName") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    val test = for (lastName <- generator.lastName) yield assert(lastName == "Richard")
    test.unsafeRunSync()
  }

  test("It should generate prefix") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    val test = for (lastName <- generator.prefix) yield assert(lastName == "Mr")
    test.unsafeRunSync()
  }

  test("It should generate suffix") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    val test = for (lastName <- generator.suffix) yield assert(lastName == "I")
    test.unsafeRunSync()
  }

  test("It should generate full name") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    val test = for (lastName <- generator.fullName) yield assert(lastName == "John Richard")
    test.unsafeRunSync()
  }

  test("It should generate full name with prefix") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    val test = for (lastName <- generator.fullName(true, false))
      yield assert(lastName == "Mr John Richard")
    test.unsafeRunSync()
  }

  test("It should generate full name with suffix") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    val test = for (lastName <- generator.fullName(false, true))
      yield assert(lastName == "John Richard I")
    test.unsafeRunSync()
  }

  test("It should generate full name with prefix and suffix") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    generator.fullName(true, true).map(lastName => assert(lastName == "Mr John Richard I")).unsafeRunSync()
  }

  test("It should generate username") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1000).returning(45)

    generator.username.map(username => assert(username == "john_richard_45")).unsafeRunSync()
  }
}
