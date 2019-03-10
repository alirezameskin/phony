package phony.generators

import phony.data._
import phony.resource.{LocaleProvider, SyncLocale}
import phony.{Locale, RandomUtility}
import cats.effect.IO
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite

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
  implicit val random = mock[RandomUtility[IO]]
  val generator = new LoremGenerator[IO]

  test("It should select one word from the words list") {
    (random.randomItem(_: Seq[String])).expects(dataProvider.lorem.words).returning(IO("back"))

    generator.word.map(word => assert(word == "back")).unsafeRunSync()
  }

  test("It should return five words from the words list") {
    (random
      .randomItems(_: Int)(_: Seq[String]))
      .expects(5, dataProvider.lorem.words)
      .returning(IO(List("bag", "bake", "bird", "background", "bad")))

    generator
      .words(5)
      .map { words =>
        words.foreach(word => assert(dataProvider.lorem.words.contains(word)))
      }
      .unsafeRunSync()
  }
}
