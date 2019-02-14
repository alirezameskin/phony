package alaki.generators

import alaki.RandomUtility
import cats.Monad
import cats.implicits._

import scala.language.higherKinds

class AlphanumericGenerator[F[_]: Monad](val utility: RandomUtility) {

  def boolean: F[Boolean] = utility.nextBoolean.pure[F]

  def float: F[Float] = utility.nextFloat.pure[F]

  def number: F[Int] = number(100000)

  def number(max: Int): F[Int] = utility.nextInt(max).pure[F]

  def number(min: Int, max: Int): F[Int] = (utility.nextInt(max - min) + min).pure[F]
}
