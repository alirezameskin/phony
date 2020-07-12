package phony.cli.template.tokenizer

sealed trait TemplateToken
case class Expression(expr: String) extends TemplateToken
case class PlainText(value: String) extends TemplateToken
