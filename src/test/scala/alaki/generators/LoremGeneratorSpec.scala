package alaki.generators

import alaki.Locale
import alaki.data._
import alaki.resource.{LocaleProvider, SyncLocale}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

import scala.util.Random

class LoremGeneratorSpec extends FunSuite with MockFactory {
  val dataProvider = LocaleProvider(
    LoremData(
      Vector("back", "background", "bad", "badly", "bag", "bake", "dolores", "et", "bind", "biological", "bird")
    ),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[IO] = new SyncLocale[IO](IO(dataProvider))
  val random = mock[Random]
  val generator = new LoremGenerator[IO](random)

  test("It should select one word from the words list") {
    (random.nextInt(_: Int)).expects(11).returning(0)

    generator.word.map(word => assert(word == "back")).unsafeRunSync()
  }

  test("It should return five words from the words list") {
    generator
      .words(5)
      .map { words =>
        words.foreach(word => assert(dataProvider.lorem.words.contains(word)))
      }
      .unsafeRunSync()
  }
}
