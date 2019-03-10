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
    utility.nextChar

  def boolean: F[Boolean] =
    utility.nextBoolean

  def float: F[Float] =
    utility.nextFloat

  def number: F[Int] =
    number(100000)

  def number(max: Int): F[Int] =
    utility.nextInt(max)

  def number(min: Int, max: Int): F[Int] =
    implicitly[Monad[F]].map(utility.nextInt(max - min))(_ + min)
}
