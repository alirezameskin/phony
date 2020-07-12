package phony.cli.template.parser

import phony.cli
import phony.cli.template.tokenizer.{Expression, PlainText, TemplateToken}
import phony.cli.util.BaseRegexParser

object TemplateExpressionParser extends BaseRegexParser {

  def functionName: Parser[String] =
    ident ~ rep(ident | ".") ^^ (n => n._1 + n._2.mkString(""))

  def paramString: Parser[String] = """\"[0-9a-zA-Z\-"#\?]+\"""".r

  def functionWithoutArgs: Parser[FunctionCall] =
    functionName ^^ (n => FunctionCall(n))

  def functionWithArgs: Parser[FunctionCall] =
    functionName ~ "(" ~ repsep(paramString | decimalNumber, ",") ~ ")" ^^ {
      case func ~ _ ~ args ~ _ => FunctionCall(func, args)
    }

  def program: Parser[TemplateAST] =
    phrase(functionWithArgs | functionWithoutArgs)

  def apply(tokens: Seq[TemplateToken]): Either[Throwable, List[TemplateAST]] = {
    val parts = tokens.map {
      case Expression(expr) =>
        parse(program, expr) match {
          case Success(result, _) => Right(result)
          case NoSuccess(msg, _) => Left(new Exception(s"Error: $msg"))
        }
      case PlainText(v) => Right(Text(v))
    }.toList

    cli.util.sequence(parts)
  }
}
