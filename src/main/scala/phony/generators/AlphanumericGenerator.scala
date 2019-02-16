package phony.generators

import cats.Monad
import cats.implicits._
import phony.RandomUtility

import scala.language.higherKinds

class AlphanumericGenerator[F[_]: Monad](val utility: RandomUtility) {

  /**
    * Generates custom using ascii uppercase and random integers
    * replaces # with integer and replaces ? with Ascii character
    *
    * @param format
    * @return
    * @example custom("##??")
    */
  def custom(format: String): F[String] = utility.bothify(format).pure[F]

  def char: F[Char] = utility.nextChar.pure[F]

  def boolean: F[Boolean] = utility.nextBoolean.pure[F]

  def float: F[Float] = utility.nextFloat.pure[F]

  def number: F[Int] = number(100000)

  def number(max: Int): F[Int] = utility.nextInt(max).pure[F]

  def number(min: Int, max: Int): F[Int] = (utility.nextInt(max - min) + min).pure[F]
}
