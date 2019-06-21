package phony.generators

import cats.implicits._
import org.scalatest.Matchers._
import org.scalatest.TryValues._
import org.scalatest.{FunSuite, _}
import phony.data._
import phony.resource.{DefaultLocale, LocaleProvider}
import phony.{Locale, MonadRandomUtility}

import scala.util.Try

class NameGeneratorSpec extends FunSuite {

  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector("John"), Vector("Richard"), Vector("Mr"), Vector("I")),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[Try] = new DefaultLocale[Try](Try(dataProvider))
  implicit val random = new MonadRandomUtility[Try]
  val generator = new NameGenerator[Try]

  test("It should generate firstName") {
    generator.firstName.success.value shouldBe "John"
  }

  test("It should generate lastName") {
    generator.lastName.success.value shouldBe "Richard"
  }

  test("It should generate prefix") {
    generator.prefix.success.value shouldBe "Mr"
  }

  test("It should generate suffix") {
    generator.suffix.success.value shouldBe "I"
  }

  test("It should generate full name") {
    generator.fullName.success.value shouldBe "John Richard"
  }

  test("It should generate full name with prefix") {
    generator.fullName(true, false).success.value shouldBe "Mr John Richard"
  }

  test("It should generate full name with suffix") {
    generator.fullName(false, true).success.value shouldBe "John Richard I"
  }

  test("It should generate full name with prefix and suffix") {
    generator.fullName(true, true).success.value shouldBe "Mr John Richard I"
  }

  test("It should generate username") {
    generator.username.success.value should fullyMatch regex "john_richard_([0-9]+)"
  }
}
