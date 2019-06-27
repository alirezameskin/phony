package phony.generators

import cats.implicits._
import org.scalatest.Matchers._
import org.scalatest.TryValues._
import org.scalatest._
import phony.data._
import phony.resource.{DefaultLocale, LocaleProvider}
import phony.{Locale, MonadRandomUtility}

import scala.util.Try

class LoremGeneratorSpec extends FunSuite {
  val dataProvider = LocaleProvider(
    LoremData(
      Vector("back", "background", "bad", "badly", "bag", "bake", "dolores", "et", "bind", "biological", "bird")
    ),
    NameData(Vector.empty, Vector.empty, Vector.empty, Vector.empty),
    InternetData(Vector.empty, Vector.empty),
    CalendarData(Vector.empty, Vector.empty),
    LocationData(Vector.empty)
  )

  implicit val locale: Locale[Try] = new DefaultLocale[Try](Try(dataProvider))
  implicit val random = new MonadRandomUtility[Try]()
  val generator = new LoremGenerator[Try]

  test("It should select one word from the words list") {
    val word = generator.word.success.value

    dataProvider.lorem.words should contain(word)
  }

  test("It should return five words from the words list") {
    val words = generator.words(5).success.value
    words should have size 5
    words.foreach { word =>
      dataProvider.lorem.words should contain(word)
    }

  }
}
