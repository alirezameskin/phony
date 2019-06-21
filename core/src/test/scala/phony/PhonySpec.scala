package phony

import org.scalatest.FunSuite
import phony.data._
import phony.resource.{DefaultLocale, LocaleProvider}
import cats.implicits._

import scala.util.Try

class PhonySpec extends FunSuite {
  val dataProvider = LocaleProvider(
    LoremData(Vector("Word1")),
    NameData(Vector("First Name"), Vector("Last Name"), Vector("Prefix"), Vector("Suffix")),
    InternetData(Vector("email"), Vector(".test")),
    CalendarData(Vector("Monday"), Vector("May")),
    LocationData(Vector(Country("Germany", "de")))
  )

  implicit val locale: Locale[Try] = new DefaultLocale[Try](Try(dataProvider))
  implicit val utility: RandomUtility[Try] = new MonadRandomUtility[Try]
  val phony: Phony[Try] = new Phony[Try]

  test("Generator should generate data") {
    for (firstName <- phony.name.firstName) yield assert(firstName == "First Name")
    for (lastName <- phony.name.lastName) yield assert(lastName == "Last Name")
    for (lastName <- phony.name.prefix) yield assert(lastName == "Prefix")
    for (lastName <- phony.name.suffix) yield assert(lastName == "Suffix")
    for (lastName <- phony.name.fullName) yield assert(lastName == "First Name Last Name")
  }
}
