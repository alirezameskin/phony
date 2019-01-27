package alaki.generators

import alaki.Locale
import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.util.Random

class LocationGeneratorSpec extends FunSuite with MockFactory {
  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector(Country("Germany", "DE"), Country("Iran", "IR")))
  )

  val random = mock[Random]

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val generator = new LocationGenerator[IO](random)

  test("It should generate a latitude") {
    (random.nextDouble _).expects().returning(0.6211648765055653)
    generator.latitude
      .map(lat => assert(lat == "21.809678"))
      .unsafeRunSync
  }

  test("It should generate a longitude") {
    (random.nextDouble _).expects().returning(0.3682367445255824)
    generator.longitude
      .map(lng => assert(lng == "-47.434772"))
      .unsafeRunSync
  }

  test("It should select one country name from the available countries") {
    (random.nextInt(_: Int)).expects(2).returning(1)
    generator.countryName
      .map(name => assert(name == "Iran"))
      .unsafeRunSync
  }

  test("It should select one country code from the available countries") {
    (random.nextInt(_: Int)).expects(2).returning(1)
    generator.countryCode
      .map(code => assert(code == "IR"))
      .unsafeRunSync
  }

  test("It should select one country from the available countries") {
    (random.nextInt(_: Int)).expects(2).returning(0)
    generator.country
      .map(country => assert(country == Country("Germany", "DE")))
      .unsafeRunSync
  }
}
