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
    letterify(format).flatMap(numerify)

  override def nextBoolean: F[Boolean] =
    random.nextBoolean.pure[F]

  override def nextDouble: F[Double] =
    random.nextDouble.pure[F]

  override def nextFloat: F[Float] =
    random.nextFloat.pure[F]

  override def nextChar: F[Char] =
    (random.nextInt(25) + 65).toChar.pure[F]

  override def nextInt: F[Int] =
    random.nextInt.pure[F]

  override def nextInt(max: Int): F[Int] =
    random.nextInt(max).pure[F]

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
