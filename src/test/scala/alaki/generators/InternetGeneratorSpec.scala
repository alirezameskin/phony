package alaki.generators

import alaki.Locale
import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.util.Random

class InternetGeneratorSpec extends FunSuite with MockFactory {
  val dataProvider = LocaleProvider(
    LoremData(Vector.empty),
    NameData(Vector("John"), Vector("Smith"), Vector.empty, Vector.empty),
    InternetData(Vector("Yahoo.com", "gmail.com"), Vector(".co", ".com")),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val random = mock[Random]
  val generator = new InternetGenerator[IO](random)

  test("It should generate an Email") {
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(1).returning(0)
    (random.nextInt(_: Int)).expects(2).returning(0)
    generator.email.map(email => assert(email == "john.smith@yahoo.com")).unsafeRunSync
  }

  test("It should generate a valid UUID") {
    generator.uuid
      .map(
        uuid => assert(uuid.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"))
      )
      .unsafeRunSync
  }

  test("It should select a domain suffix from available domains") {
    (random.nextInt(_: Int)).expects(2).returning(1)

    generator.domain
      .map(domain => assert(domain == ".com"))
      .unsafeRunSync
  }

}
