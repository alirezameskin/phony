package phony.generators

import cats.Monad
import phony.RandomUtility

import scala.language.higherKinds

class AlphanumericGenerator[F[_]: Monad](implicit val utility: RandomUtility[F]) {

  /**
   * Generates custom using ascii uppercase and random integers
   * replaces # with integer and replaces ? with Ascii character
   *
   * @param format
   * @return
   * @example custom("##??")
   */
  def custom(format: String): F[String] =
    utility.bothify(format)

  def char: F[Char] =
    utility.char

  def boolean: F[Boolean] =
    utility.boolean

  def float: F[Float] =
    utility.float

  def number: F[Int] =
    number(100000)

  def number(max: Int): F[Int] =
    utility.int(max)

  def number(min: Int, max: Int): F[Int] =
    Monad[F].map(utility.int(max - min))(_ + min)
}
