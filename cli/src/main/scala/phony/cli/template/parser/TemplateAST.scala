package phony.cli.template.parser

sealed trait TemplateAST
case class Text(value: String) extends TemplateAST
case class FunctionCall(func: String, args: Seq[String] = Nil) extends TemplateAST
