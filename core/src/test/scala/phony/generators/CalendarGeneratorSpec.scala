package phony.generators

import cats.implicits._
import org.scalatest.TryValues._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers._
import phony.data._
import phony.resource.{DefaultLocale, LocaleProvider}
import phony.{Locale, MonadRandomUtility}

import scala.util.Try

class CalendarGeneratorSpec extends AnyFunSuite {
  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector("Monday", "Wednesday"), Vector("May", "June", "April")),
    LocationData(Vector.empty, Vector.empty),
    ContactData(Vector.empty, Vector.empty)
  )

  implicit val locale: Locale[Try] = new DefaultLocale[Try](Try(dataProvider))
  implicit val random = new MonadRandomUtility[Try]()
  val generator = new CalendarGenerator[Try]

  test("It should return a year between 1970 and 2019") {
    val year = generator.year.success.value
    year should be >= 1970
    year should be <= 2025
  }

  test("It should return one day from the available day names") {
    val day = generator.day.success.value
    dataProvider.calendar.days should contain(day)
  }

  test("It should return a month from the available months") {
    val month = generator.month.success.value

    dataProvider.calendar.months should contain(month)
  }

  test("It should return a 24H time") {
    (generator.time24h.success.value should fullyMatch).regex("[0-2][0-9]:[0-6][0-9]")
  }

  test("It should return a 12H time") {
    (generator.time12h.success.value should fullyMatch).regex("[0-1][0-9]:[0-6][0-9]")
  }

  test("It should return a date in Date format") {
    generator.date.success.value
  }

  test("It should return a date string in format yyyy-MM-dd") {
    (generator.date("yyyy-MM-dd").success.value should fullyMatch).regex("[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}")
  }

  test("It should return a date string in iso 8601 format") {
    generator.iso8601.success.value
  }

  test("It should return a unix timestamp") {
    generator.unixTime.success.value
  }

  test("It should return a timezone from the available timezones") {
    generator.timezone.success.value
  }
}
