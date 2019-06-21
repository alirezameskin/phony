package phony.generators

import cats.implicits._
import org.scalatest.Matchers._
import org.scalatest.TryValues._
import org.scalatest._
import phony.data._
import phony.resource.{DefaultLocale, LocaleProvider}
import phony.{Locale, MonadRandomUtility}

import scala.util.Try

class AlphanumericGeneratorSpec extends FunSuite {

  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[Try] = new DefaultLocale[Try](Try(dataProvider))
  implicit val random = new MonadRandomUtility[Try]()
  val generator = new AlphanumericGenerator[Try]

  test("It should generate a string with ###??? format") {
    generator.custom("###???").success.value should fullyMatch regex "[0-9]{3}[A-Z]{3}"
  }

  test("It should generate a character") {
    generator.char.map(_.toString).success.value should fullyMatch regex "[A-Z]"
  }

  test("It should return one boolean if boolean method is called") {
    val boolean = generator.boolean.success.value
    assert(boolean == true || boolean == false)
  }

  test("It should generate a float number if float method is called") {
    generator.float.map(_.toString).success.value should fullyMatch regex "[-+]?([0-9]*\\.[0-9]+|[0-9]+)."
  }

  test("It should generate an integer if number method is called") {
    generator.number.map(_.toString).success.value should fullyMatch regex "[0-9]+"
  }

  test("It should generate an integer less than 100 ") {
    val number = generator.number(100).success.value

    number should be <= 100
    number should be >= 0
  }

  test("It should generate an integer between 100 and 600") {
    val number = generator.number(100, 600).success.value

    number should be >= 100
    number should be <= 600
  }
}

