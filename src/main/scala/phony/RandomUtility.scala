package phony

import java.util.UUID

import scala.language.higherKinds

trait RandomUtility[F[_]] {
  def numerify(format: String): F[String]

  def letterify(format: String): F[String]

  def bothify(format: String): F[String]

  def nextBoolean: F[Boolean]

  def nextDouble: F[Double]

  def nextFloat: F[Float]

  def nextChar: F[Char]

  def nextInt: F[Int]

  def nextInt(max: Int): F[Int]

  def nextLong: F[Long]

  def nextUUID: F[UUID]

  def randomItem[A](items: Seq[A]): F[A]

  def randomItems[A](count: Int)(items: Seq[A]): F[List[A]]
}
