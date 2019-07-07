package phony

import java.util.UUID

import _root_.cats.implicits._
import cats.Monad

import scala.language.higherKinds
import scala.util.Random

class MonadRandomUtility[F[_]: Locale](implicit M: Monad[F]) extends RandomUtility[F] {
  protected val random: Random = new Random()

  override def numerify(format: String): F[String] =
    "#".r.findAllIn(format).toList.foldLeftM(format) { (text, _) =>
      int(9).map(int => text.replaceFirst("#", int.toString))
    }

  override def letterify(format: String): F[String] =
    """\?""".r.findAllIn(format).toList.foldLeftM(format) { (text, _) =>
      char.map(char => text.replaceFirst("""\?""", char.toString))
    }

  override def bothify(format: String): F[String] =
    letterify(format) >>= numerify

  override def boolean: F[Boolean] =
    M.pure(random.nextBoolean)

  override def double: F[Double] =
    M.pure(random.nextDouble)

  override def float: F[Float] =
    M.pure(random.nextFloat)

  override def char: F[Char] =
    M.pure(random.nextInt(25) + 65).map(_.toChar)

  override def int: F[Int] =
    M.pure(random.nextInt)

  override def int(max: Int): F[Int] =
    M.pure(random.nextInt(max))

  override def long: F[Long] =
    M.pure(random.nextLong)

  override def uuid: F[UUID] =
    M.pure(java.util.UUID.randomUUID)

  override def randomItem[A](items: Seq[A]): F[A] =
    int(items.size).map(items(_))

  override def randomItems[A](count: Int)(items: Seq[A]): F[List[A]] =
    (1 to count).toList.foldLeftM(List.empty[A]) { (list: List[A], _) =>
      items match {
        case Nil => List.empty[A].pure[F]
        case array =>
          int(array.size)
            .map(i => array(i))
            .map(item => item +: list)
      }
    }
}
