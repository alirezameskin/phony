package phony.instances

import cats.implicits._
import phony.resource.DefaultLocale
import phony.{Locale, MonadRandomUtility, Phony, RandomUtility}

trait EitherInstances {
  implicit def utility: RandomUtility[Either[Throwable, ?]] =
    new MonadRandomUtility[Either[Throwable, ?]]()

  implicit def eval(implicit locale: Locale[Either[Throwable, ?]]): Phony[Either[Throwable, ?]] =
    new Phony[Either[Throwable, ?]]

  object languages {
    implicit def ENGLISH: Locale[Either[Throwable, ?]] = DefaultLocale[Either[Throwable, ?]]("en")
  }
}
