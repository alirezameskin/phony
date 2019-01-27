package alaki

import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import cats.effect.IO
import org.scalatest.FunSuite

class GeneratorSpec extends FunSuite {
  val dataProvider = LocaleProvider(
    LoremData(Vector("Word1")),
    NameData(Vector("First Name"), Vector("Last Name"), Vector("Prefix"), Vector("Suffix")),
    InternetData(Vector("email"), Vector(".test")),
    CalendarData(Vector("Monday"), Vector("May")),
    LocationData(Vector(Country("Germany", "de")))
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val generator: Generator[IO] = new SyncGenerator[IO]

  test("Generator should generate data") {
    val firstNameTest = for (firstName <- generator.name.firstName) yield assert(firstName == "First Name")
    val lastNameTest = for (lastName <- generator.name.lastName) yield assert(lastName == "Last Name")
    val prefixTest = for (lastName <- generator.name.prefix) yield assert(lastName == "Prefix")
    val suffixTest = for (lastName <- generator.name.suffix) yield assert(lastName == "Suffix")
    val fullNameTest = for (lastName <- generator.name.fullName) yield assert(lastName == "First Name Last Name")

    firstNameTest.unsafeRunSync()
    lastNameTest.unsafeRunSync()
    prefixTest.unsafeRunSync()
    suffixTest.unsafeRunSync()
    fullNameTest.unsafeRunSync()
  }

}
