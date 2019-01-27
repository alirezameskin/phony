package alaki.generators

import alaki.Locale
import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import cats.effect.IO
import org.scalatest.FunSuite

import scala.util.Random

class NameGeneratorSpec extends FunSuite {

  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector("John"), Vector("Richard"), Vector("Mr"), Vector("I")),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val random = new Random()
  val generator = new NameGenerator[IO](random)

  test("It should generate firstName") {
    val test = for (firstName <- generator.firstName) yield assert(firstName == "John")
    test.unsafeRunSync()
  }

  test("It should generate lastName") {
    val test = for (lastName <- generator.lastName) yield assert(lastName == "Richard")
    test.unsafeRunSync()
  }

  test("It should generate prefix") {
    val test = for (lastName <- generator.prefix) yield assert(lastName == "Mr")
    test.unsafeRunSync()
  }

  test("It should generate suffix") {
    val test = for (lastName <- generator.suffix) yield assert(lastName == "I")
    test.unsafeRunSync()
  }

  test("It should generate full name") {
    val test = for (lastName <- generator.fullName) yield assert(lastName == "John Richard")
    test.unsafeRunSync()
  }

  test("It should generate full name with prefix") {
    val test = for (lastName <- generator.fullName(true, false))
      yield assert(lastName == "Mr John Richard")
    test.unsafeRunSync()
  }

  test("It should generate full name with suffix") {
    val test = for (lastName <- generator.fullName(false, true))
      yield assert(lastName == "John Richard I")
    test.unsafeRunSync()
  }

  test("It should generate full name with prefix and suffix") {
    val test = for (lastName <- generator.fullName(true, true))
      yield assert(lastName == "Mr John Richard I")
    test.unsafeRunSync()
  }
}
