package phony.generators

import java.util.GregorianCalendar

import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import phony.data._
import phony.resource.{LocaleProvider, SyncLocale}
import phony.{Locale, RandomUtility}

class CalendarGeneratorSpec extends FunSuite with MockFactory {
  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector("Monday", "Wednesday"), Vector("May", "June", "April")),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  implicit val random = mock[RandomUtility[IO]]
  val generator = new CalendarGenerator[IO]

  test("It should return a year between 1970 and 2019") {
    (random.nextInt(_: Int)).expects(49).returning(IO(12))

    generator.year.map(year => assert(year == 1982)).unsafeRunSync()
  }

  test("It should return one day from the available day names") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.calendar.days).returning(IO("Wednesday"))
    generator.day.map(day => assert(day == "Wednesday")).unsafeRunSync()
  }

  test("It should return a month from the available months") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.calendar.months).returning(IO("April"))
    generator.month.map(month => assert(month == "April")).unsafeRunSync()
  }

  test("It should return a 24H time") {
    (random.nextInt(_: Int)).expects(24).returning(IO(9))
    (random.nextInt(_: Int)).expects(60).returning(IO(7))
    generator.time24h.map(time => assert(time == "09:07")).unsafeRunSync()
  }

  test("It should return a 12H time") {
    (random.nextInt(_: Int)).expects(12).returning(IO(8))
    (random.nextInt(_: Int)).expects(60).returning(IO(23))
    generator.time12h.map(time => assert(time == "08:23")).unsafeRunSync()
  }

  test("It should return a date in Date format") {
    (random.nextInt(_: Int)).expects(49).returning(IO(40))
    (random.nextInt(_: Int)).expects(12).returning(IO(8))
    (random.nextInt(_: Int)).expects(31).returning(IO(18))
    (random.nextInt(_: Int)).expects(24).returning(IO(9))
    (random.nextInt(_: Int)).expects(60).returning(IO(7))
    (random.nextInt(_: Int)).expects(60).returning(IO(17))

    val expectedDate = new GregorianCalendar(2010, 8, 18, 9, 7, 17).getTime
    generator.date.map(date => assert(date.compareTo(expectedDate) == 0)).unsafeRunSync()
  }

  test("It should return a date string in format yyyy-MM-dd") {
    (random.nextInt(_: Int)).expects(49).returning(IO(35))
    (random.nextInt(_: Int)).expects(12).returning(IO(11))
    (random.nextInt(_: Int)).expects(31).returning(IO(10))
    (random.nextInt(_: Int)).expects(24).returning(IO(15))
    (random.nextInt(_: Int)).expects(60).returning(IO(6))
    (random.nextInt(_: Int)).expects(60).returning(IO(27))

    generator.date("yyyy-MM-dd").map(date => assert(date == "2005-12-10")).unsafeRunSync()
  }
}
