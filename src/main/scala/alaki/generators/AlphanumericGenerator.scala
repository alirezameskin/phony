package alaki.generators

import cats.Monad
import cats.implicits._

import scala.util.Random

class AlphanumericGenerator[F[_]: Monad](val random: Random) extends DataGenerator {

  def boolean: F[Boolean] = random.nextBoolean.pure[F]

  def float: F[Float] = random.nextFloat.pure[F]

  def number: F[Int] = random.nextInt(1000).pure[F]

  def number(max: Int): F[Int] = random.nextInt(max).pure[F]

  def number(min: Int, max: Int): F[Int] = (random.nextInt(max - min) + min).pure[F]
}
