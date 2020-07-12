package phony.cli

package object util {
  def sequence[A, B](ls: List[Either[A, B]]): Either[A, List[B]] =
    ls.foldRight(Right(List.empty[B]): Either[A, List[B]]) { (e, acc) =>
      for (r  <- e;
           rs <- acc) yield r :: rs
    }

}
