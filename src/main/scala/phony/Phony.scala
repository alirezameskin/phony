package phony

import cats.Monad
import cats.effect.IO
import phony.generators._

class Phony[F[_]: Monad: Locale: RandomUtility] {

  val alphanumeric = new AlphanumericGenerator[F]
  val calendar = new CalendarGenerator[F]
  val internet = new InternetGenerator[F]
  val lorem = new LoremGenerator[F]
  val name = new NameGenerator[F]
  val location = new LocationGenerator[F]
}

object Phony {
  implicit val utility: RandomUtility[IO] = new SyncRandomUtility[IO]()
  implicit val english: Locale[IO] = Locale.ENGLISH
  implicit val io: Phony[IO] = new SyncPhony[IO]

  def apply[F[_]](implicit F: Phony[F], locale: Locale[F]): Phony[F] = F
}
