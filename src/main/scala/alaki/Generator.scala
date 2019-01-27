package alaki

import alaki.generators._
import cats.Monad
import cats.effect.IO

import scala.util.Random

class Generator[F[_]](implicit A: Monad[F], locale: Locale[F]) {
  private val random: Random = new Random()

  val alphanumeric = new AlphanumericGenerator[F](random)
  val calendar = new CalendarGenerator[F](random)
  val internet = new InternetGenerator[F](random)
  val lorem = new LoremGenerator[F](random)
  val name = new NameGenerator[F](random)
  val location = new LocationGenerator[F](random)
}

object Generator {
  implicit val english: Locale[IO] = Locale.ENGLISH
  implicit val io: Generator[IO] = new SyncGenerator[IO]

  def apply[F[_]](implicit F: Generator[F], locale: Locale[F]): Generator[F] = F
}
