package phony.generators

import cats.implicits._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers._
import org.scalatest.TryValues._
import phony.data._
import phony.resource.{DefaultLocale, LocaleProvider}
import phony.{Locale, MonadRandomUtility}

import scala.util.Try

class LocationGeneratorSpec extends AnyFunSuite {
  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector(CountryData("Germany", "DE"), CountryData("Iran", "IR")), Vector.empty),
    ContactData(Vector.empty, Vector.empty)
  )

  implicit val locale: Locale[Try] = new DefaultLocale[Try](Try(dataProvider))
  implicit val random = new MonadRandomUtility[Try]
  val generator = new LocationGenerator[Try]

  test("It should generate a latitude") {
    (generator.latitude.success.value should fullyMatch).regex("[-+]?([0-9]*\\.[0-9]+|[0-9]+).")
  }

  test("It should generate a longitude") {
    (generator.longitude.success.value should fullyMatch).regex("[-+]?([0-9]*\\.[0-9]+|[0-9]+).")
  }

  test("It should select one country name from the available countries") {
    val country = generator.countryName.success.value

    assert(country == "Iran" || country == "Germany")
  }

  test("It should select one country code from the available countries") {
    val code = generator.countryCode.success.value
    assert(code == "IR" || code == "DE")
  }

  test("It should select one country from the available countries") {
    val country = generator.country.success.value
    assert(country == CountryData("Germany", "DE") || country == CountryData("Iran", "IR"))
  }
}
