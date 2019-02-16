package phony

import cats.Monad
import cats.effect.IO
import phony.generators._

class Phony[F[_]](implicit A: Monad[F], locale: Locale[F]) {

  private val utility = new RandomUtility();

  val alphanumeric = new AlphanumericGenerator[F](utility)
  val calendar = new CalendarGenerator[F](utility)
  val internet = new InternetGenerator[F](utility)
  val lorem = new LoremGenerator[F](utility)
  val name = new NameGenerator[F](utility)
  val location = new LocationGenerator[F](utility)
}

object Phony {
  implicit val english: Locale[IO] = Locale.ENGLISH
  implicit val io: Phony[IO] = new SyncPhony[IO]

  def apply[F[_]](implicit F: Phony[F], locale: Locale[F]): Phony[F] = F
}
