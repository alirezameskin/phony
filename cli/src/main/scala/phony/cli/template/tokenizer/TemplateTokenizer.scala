package phony.cli.template.tokenizer

import phony.cli.util.BaseRegexParser

object TemplateTokenizer extends BaseRegexParser {
  val expressionRegex = """\{\{[a-z_A-Z0-9\|\s\.\(\)\"\,#]+\}\}""".r

  def plainText: Parser[PlainTextToken] =
    notMatch(expressionRegex) ^^ PlainTextToken

  def expression: Parser[FunctionCallToken] = functionWithArgs | functionWithoutArgs

  def functionName: Parser[String] =
    ident ~ rep(ident | ".") ^^ (n => n._1 + n._2.mkString(""))

  def functionWithoutArgs: Parser[FunctionCallToken] =
    """{{""" ~> functionName <~ """}}""" ^^ (n => FunctionCallToken(n))

  def functionWithArgs: Parser[FunctionCallToken] =
    """{{""" ~> functionName ~ """(""" ~ repsep(paramString | decimalNumber, ",") ~ """)""" <~ """}}""".r ^^ {
      case func ~ _ ~ args ~ _ => FunctionCallToken(func, args)
    }

  def paramString: Parser[String] = """\"[0-9a-zA-Z\-"#\?]+\"""".r

  def loopStart: Parser[LoopStartToken] = """{{#loop""" ~ decimalNumber ~ """}}""" ^^ {
    case _ ~ number ~ _ => LoopStartToken(number.toInt)
  }

  def loopEnd: Parser[LoopEndToken.type] = "{{#endloop}}" ^^ { _ =>
    LoopEndToken
  }

  def tokens: Parser[List[TemplateToken]] =
    rep1(loopStart | loopEnd | expression | plainText)

  def apply(str: String): Either[Throwable, List[TemplateToken]] =
    parse(tokens, str) match {
      case Success(result, _) => Right(result)
      case NoSuccess(msg, _)  => Left(new Exception(msg))
    }

}
