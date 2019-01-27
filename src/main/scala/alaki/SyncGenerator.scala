package alaki

import cats.effect.Sync

import scala.language.higherKinds

/**
 * A default instance for any `F[_]` with a `Sync` instance.
 */
private class SyncGenerator[F[_]](implicit F: Sync[F], L: Locale[F]) extends Generator[F]
