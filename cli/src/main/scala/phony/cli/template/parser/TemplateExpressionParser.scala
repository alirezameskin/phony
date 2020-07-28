package phony.cli.template.parser

import phony.cli.template.tokenizer._

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

object TemplateExpressionParser extends Parsers {
  override type Elem = TemplateToken

  class TemplateTokenReader(tokens: Seq[TemplateToken]) extends Reader[TemplateToken] {
    override def first: TemplateToken        = tokens.head
    override def atEnd: Boolean              = tokens.isEmpty
    override def pos: Position               = NoPosition
    override def rest: Reader[TemplateToken] = new TemplateTokenReader(tokens.tail)
  }

  def functionCall: Parser[FunctionCall] = accept(
    "functionCall", {
      case f: FunctionCallToken => FunctionCall(f.func, f.args)
    }
  )

  def plainText: Parser[Text] = accept(
    "text", {
      case t: PlainTextToken => Text(t.value)
    }
  )

  def loopStart: Parser[Int] = accept(
    "loopStart", {
      case t: LoopStartToken => t.count
    }
  )

  def loop: Parser[Loop] =
    loopStart ~ rep(plainText | functionCall) <~ LoopEndToken ^^ {
      case c ~ tokens => Loop(c, tokens)
    }

  def program: Parser[List[TemplateAST]] =
    rep(loop | functionCall | plainText)

  def apply(tokens: List[TemplateToken]): Either[Throwable, List[TemplateAST]] = {
    val reader = new TemplateTokenReader(tokens)

    program(reader) match {
      case Success(result, _) => Right(result)
      case NoSuccess(msg, _)  => Left(new RuntimeException(msg))
    }
  }
}
