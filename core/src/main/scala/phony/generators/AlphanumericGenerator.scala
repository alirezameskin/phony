package phony.generators

import cats.Monad
import cats.implicits.toFunctorOps
import phony.RandomUtility

import scala.language.higherKinds

class AlphanumericGenerator[F[_]: Monad: RandomUtility] {

  /**
   * Generates custom using ascii uppercase and random integers
   * replaces # with integer and replaces ? with Ascii character
   *
   * @param format
   * @return
   * @example custom("##??")
   */
  def custom(format: String): F[String] =
    RandomUtility[F].bothify(format)

  def char: F[Char] =
    RandomUtility[F].char

  def boolean: F[Boolean] =
    RandomUtility[F].boolean

  def float: F[Float] =
    RandomUtility[F].float

  def number: F[Int] =
    number(100000)

  def number(max: Int): F[Int] =
    RandomUtility[F].int(max)

  def hash: F[String] =
    hash(40)

  def hash(length: Int): F[String] =
    RandomUtility[F].randomItems(length)("0123456789abcdef".toList).map(_.mkString)

  def number(min: Int, max: Int): F[Int] =
    RandomUtility[F].int(max - min)
}
