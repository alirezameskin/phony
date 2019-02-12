package alaki.generators

import java.util.GregorianCalendar

import alaki.Locale
import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.util.Random

class CalendarGeneratorSpec extends FunSuite with MockFactory {
  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector("Monday", "Wednesday"), Vector("May", "June", "April")),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val random = mock[Random]
  val generator = new CalendarGenerator[IO](random)

  test("It should return a year between 1970 and 2019") {
    (random.nextInt(_: Int)).expects(49).returning(12)

    generator.year.map(year => assert(year == 1982)).unsafeRunSync()
  }

  test("It should return one day from the available day names") {
    (random.nextInt(_: Int)).expects(2).returning(1)
    generator.day.map(day => assert(day == "Wednesday")).unsafeRunSync()
  }

  test("It should return a month from the available months") {
    (random.nextInt(_: Int)).expects(3).returning(2)
    generator.month.map(month => assert(month == "April")).unsafeRunSync()
  }

  test("It should return a 24H time") {
    (random.nextInt(_: Int)).expects(24).returning(9)
    (random.nextInt(_: Int)).expects(60).returning(7)
    generator.time24h.map(time => assert(time == "09:07")).unsafeRunSync()
  }

  test("It should return a 12H time") {
    (random.nextInt(_: Int)).expects(12).returning(8)
    (random.nextInt(_: Int)).expects(60).returning(23)
    generator.time12h.map(time => assert(time == "08:23")).unsafeRunSync()
  }

  test("It should return a date in Date format") {
    (random.nextInt(_: Int)).expects(49).returning(40)
    (random.nextInt(_: Int)).expects(12).returning(8)
    (random.nextInt(_: Int)).expects(31).returning(18)
    (random.nextInt(_: Int)).expects(24).returning(9)
    (random.nextInt(_: Int)).expects(60).returning(7)
    (random.nextInt(_: Int)).expects(60).returning(17)

    val expectedDate = new GregorianCalendar(2010, 8, 18, 9, 7, 17).getTime
    generator.date.map(date => assert(date.compareTo(expectedDate) == 0)).unsafeRunSync()
  }

  test("It should return a date string in format yyyy-MM-dd") {
    (random.nextInt(_: Int)).expects(49).returning(35)
    (random.nextInt(_: Int)).expects(12).returning(11)
    (random.nextInt(_: Int)).expects(31).returning(10)
    (random.nextInt(_: Int)).expects(24).returning(15)
    (random.nextInt(_: Int)).expects(60).returning(6)
    (random.nextInt(_: Int)).expects(60).returning(27)

    generator.date("yyyy-MM-dd").map(date => assert(date == "2005-12-10")).unsafeRunSync()
  }
}
