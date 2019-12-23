package phony

import java.util.UUID

abstract class RandomUtility[F[_]] {
  def boolean: F[Boolean]

  def double: F[Double]

  def float: F[Float]

  def char: F[Char]

  def int: F[Int]

  def int(max: Int): F[Int]

  def int(min: Int, max: Int): F[Int]

  def long: F[Long]

  def uuid: F[UUID]

  def randomItem[A](items: Seq[A]): F[A]

  def randomItems[A](count: Int)(items: Seq[A]): F[List[A]]

  def numerify(format: String): F[String]

  def letterify(format: String): F[String]

  def bothify(format: String): F[String]
}

object RandomUtility {
  @inline def apply[F[_]](implicit instance: RandomUtility[F]): RandomUtility[F] = instance
}
