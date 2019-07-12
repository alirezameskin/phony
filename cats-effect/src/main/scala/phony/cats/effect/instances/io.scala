package phony.cats.effect.instances

import cats.effect.IO
import phony.cats.effect.{SyncLocale, SyncRandomUtility}
import phony.{Locale, Phony, RandomUtility}

trait IOInstances {
  implicit def utility: RandomUtility[IO] = new SyncRandomUtility[IO]()

  implicit def io(implicit locale: Locale[IO]): Phony[IO] = new Phony[IO]

  object languages {
    implicit lazy val ENGLISH: Locale[IO] = SyncLocale[IO]("en")
  }
}
