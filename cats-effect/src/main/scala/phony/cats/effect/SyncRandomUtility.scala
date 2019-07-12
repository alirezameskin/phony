package phony.cats.effect

import java.util.UUID

import _root_.cats.effect.Sync
import _root_.cats.implicits._
import phony.RandomUtility

import scala.language.higherKinds
import scala.util.Random

class SyncRandomUtility[F[_]: Sync] extends RandomUtility[F] {
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
    Sync[F].delay(random.nextBoolean)

  override def double: F[Double] =
    Sync[F].delay(random.nextDouble)

  override def float: F[Float] =
    Sync[F].delay(random.nextFloat)

  override def char: F[Char] =
    Sync[F].delay(random.nextInt(25) + 65).map(_.toChar)

  override def int: F[Int] =
    Sync[F].delay(random.nextInt)

  override def int(max: Int): F[Int] =
    Sync[F].delay(random.nextInt(max))

  override def int(min:Int, max: Int): F[Int] =
    int(max - min).map(_ + min)

  override def long: F[Long] =
    Sync[F].delay(random.nextLong)

  override def uuid: F[UUID] =
    Sync[F].delay(java.util.UUID.randomUUID)

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
