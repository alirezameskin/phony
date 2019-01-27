package alaki.generators

import alaki.Locale
import cats.Monad
import cats.implicits._

import scala.util.Random

class LoremGenerator[F[_]: Monad](val random: Random)(implicit locale: Locale[F]) extends DataGenerator {
  def word: F[String] =
    locale.lorem.map(_.words).map(randomItem)

  def words: F[List[String]] = words(random.nextInt(20) + 5)

  def words(length: Int): F[List[String]] =
    locale.lorem.map(_.words).map(items => randomItems(length)(items)).map(_.toList)

  def sentence: F[String] = sentence(random.nextInt(20) + 10)

  def text: F[String] = text(random.nextInt(10) + 2)

  def sentence(size: Int): F[String] =
    for (list <- words(size)) yield list.mkString(" ")

  def text(lines: Int): F[String] =
    for {
      sentences <- (1 to lines).map(_ => sentence(20)).toList.sequence
    } yield sentences.mkString("\n")
}
