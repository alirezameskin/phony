package phony.generators

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class LoremGenerator[F[_]: Monad](implicit val utility: RandomUtility[F], locale: Locale[F]) {
  def word: F[String] =
    locale.lorem.map(_.words) >>= utility.randomItem

  def words: F[List[String]] =
    utility.nextInt(20).map(_ + 5) >>= words

  def words(length: Int): F[List[String]] =
    (locale.lorem.map(_.words) >>= (items => utility.randomItems(length)(items))).map(_.toList)

  def sentence: F[String] =
    utility.nextInt(20).map(_ + 10) >>= sentence

  def text: F[String] =
    utility.nextInt(10).map(_ + 2) >>= text

  def sentence(size: Int): F[String] =
    for (list <- words(size)) yield list.mkString(" ")

  def text(lines: Int): F[String] =
    for {
      sentences <- (1 to lines).map(_ => sentence(20)).toList.sequence
    } yield sentences.mkString("\n")
}
