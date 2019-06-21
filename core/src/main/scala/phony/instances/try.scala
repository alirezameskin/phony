package phony.instances

import cats.instances.try_._
import phony.resource.DefaultLocale
import phony.{Locale, MonadRandomUtility, Phony, RandomUtility}

import scala.util.Try

trait TryInstances {

  implicit def utility(implicit l: Locale[Try]): RandomUtility[Try] = new MonadRandomUtility[Try]()

  implicit def try_(implicit locale: Locale[Try]): Phony[Try] = new Phony[Try]

  object languages {
    implicit def ENGLISH: Locale[Try] = DefaultLocale[Try]("en")
  }

}
