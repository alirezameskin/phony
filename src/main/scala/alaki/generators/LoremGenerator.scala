package alaki.generators

import alaki.{Locale, RandomUtility}
import cats.Monad
import cats.implicits._

class LoremGenerator[F[_]: Monad](val utility: RandomUtility)(implicit locale: Locale[F]) {
  def word: F[String] =
    locale.lorem.map(_.words).map(utility.randomItem)

  def words: F[List[String]] = words(utility.nextInt(20) + 5)

  def words(length: Int): F[List[String]] =
    locale.lorem.map(_.words).map(items => utility.randomItems(length)(items)).map(_.toList)

  def sentence: F[String] = sentence(utility.nextInt(20) + 10)

  def text: F[String] = text(utility.nextInt(10) + 2)

  def sentence(size: Int): F[String] =
    for (list <- words(size)) yield list.mkString(" ")

  def text(lines: Int): F[String] =
    for {
      sentences <- (1 to lines).map(_ => sentence(20)).toList.sequence
    } yield sentences.mkString("\n")
}
