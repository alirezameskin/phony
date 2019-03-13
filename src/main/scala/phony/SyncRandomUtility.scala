package phony

import cats.effect.Sync
import cats.implicits._

import scala.language.higherKinds
import scala.util.Random

class SyncRandomUtility[F[_]: Sync] extends RandomUtility[F] {
  protected val random: Random = new Random()

  override def numerify(format: String): F[String] =
    "#".r.findAllIn(format).toList.foldLeftM(format) { (text, _) =>
      nextInt(9).map(int => text.replaceFirst("#", int.toString))
    }

  override def letterify(format: String): F[String] =
    """\?""".r.findAllIn(format).toList.foldLeftM(format) { (text, _) =>
      nextChar.map(char => text.replaceFirst("""\?""", char.toString))
    }

  override def bothify(format: String): F[String] =
    letterify(format) >>= numerify

  override def nextBoolean: F[Boolean] =
    Sync[F].delay(random.nextBoolean)

  override def nextDouble: F[Double] =
    Sync[F].delay(random.nextDouble)

  override def nextFloat: F[Float] =
    Sync[F].delay(random.nextFloat)

  override def nextChar: F[Char] =
    Sync[F].delay(random.nextInt(25) + 65).map(_.toChar)

  override def nextInt: F[Int] =
    Sync[F].delay(random.nextInt)

  override def nextInt(max: Int): F[Int] =
    Sync[F].delay(random.nextInt(max))

  override def randomItem[A](items: Seq[A]): F[A] =
    nextInt(items.size).map(items(_))

  override def randomItems[A](count: Int)(items: Seq[A]): F[List[A]] =
    (1 until count).toList.foldLeftM(List.empty[A]) { (list: List[A], _) =>
      items match {
        case Nil => List.empty[A].pure[F]
        case array =>
          nextInt(array.size)
            .map(i => array(i))
            .map(item => item +: list)
      }
    }
}
