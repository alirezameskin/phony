package alaki

import alaki.generators._
import cats.Monad
import cats.effect.IO

class Generator[F[_]](implicit A: Monad[F], locale: Locale[F]) {

  private val utility = new RandomUtility();

  val alphanumeric = new AlphanumericGenerator[F](utility)
  val calendar = new CalendarGenerator[F](utility)
  val internet = new InternetGenerator[F](utility)
  val lorem = new LoremGenerator[F](utility)
  val name = new NameGenerator[F](utility)
  val location = new LocationGenerator[F](utility)
}

object Generator {
  implicit val english: Locale[IO] = Locale.ENGLISH
  implicit val io: Generator[IO] = new SyncGenerator[IO]

  def apply[F[_]](implicit F: Generator[F], locale: Locale[F]): Generator[F] = F
}
