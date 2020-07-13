package phony.cli.util

import scala.util.matching.Regex
import scala.util.parsing.combinator.JavaTokenParsers

abstract class BaseRegexParser extends JavaTokenParsers {

  /** A parser that finds any string which doesn't match a regex string */
  def notMatch(r: Regex): Parser[String] = (in: Input) => {
    val source = in.source
    val offset = in.offset

    if (source.length() == offset)
      Failure("Done", in.rest)
    else
      r.findFirstMatchIn(new SubSequence(source, offset)) match {
        case Some(matched) =>
          Success(source.subSequence(offset, offset + matched.start).toString, in.drop(matched.start))
        case None =>
          Success(source.subSequence(offset, source.length).toString, in.drop(source.length - offset))
      }
  }

}
