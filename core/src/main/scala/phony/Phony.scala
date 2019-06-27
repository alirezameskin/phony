package phony

import _root_.cats.Monad
import phony.generators._

class Phony[F[_]: Monad: RandomUtility] {
  val alphanumeric = new AlphanumericGenerator[F]
  val calendar = new CalendarGenerator[F]
  val internet = new InternetGenerator[F]
  val lorem = new LoremGenerator[F]
  val name = new NameGenerator[F]
  val location = new LocationGenerator[F]
}

object Phony {
  def apply[F[_]](implicit F: Phony[F]): Phony[F] = F
}
