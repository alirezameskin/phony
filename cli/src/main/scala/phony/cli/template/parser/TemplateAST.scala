package phony.cli.template.parser

sealed trait TemplateAST
case class Text(value: String)                                 extends TemplateAST
case class FunctionCall(func: String, args: Seq[String] = Nil) extends TemplateAST
case class Loop(count: Int, templates: List[TemplateAST])      extends TemplateAST
