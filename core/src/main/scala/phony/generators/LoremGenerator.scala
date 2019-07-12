package phony.generators

import cats.Monad
import cats.implicits._
import phony.{Locale, RandomUtility}

import scala.language.higherKinds

class LoremGenerator[F[_]: Monad: Locale: RandomUtility] {
  def word: F[String] =
    Locale[F].lorem.map(_.words) >>= RandomUtility[F].randomItem

  def words: F[List[String]] =
    RandomUtility[F].int(20).map(_ + 5) >>= words

  def words(length: Int): F[List[String]] =
    (Locale[F].lorem.map(_.words) >>= (items => RandomUtility[F].randomItems(length)(items))).map(_.toList)

  def sentence: F[String] =
    RandomUtility[F].int(20).map(_ + 10) >>= sentence

  def text: F[String] =
    RandomUtility[F].int(10).map(_ + 2) >>= text

  def sentence(size: Int): F[String] =
    for (list <- words(size)) yield list.mkString(" ")

  def text(lines: Int): F[String] =
    for {
      sentences <- (1 to lines).map(_ => sentence(20)).toList.sequence
    } yield sentences.mkString("\n")
}
