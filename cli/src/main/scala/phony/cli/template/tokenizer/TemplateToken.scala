package phony.cli.template.tokenizer

sealed trait TemplateToken
case class FunctionCallToken(func: String, args: Seq[String] = Nil) extends TemplateToken
case class PlainTextToken(value: String)                            extends TemplateToken
case class LoopStartToken(count: Int)                               extends TemplateToken
case object LoopEndToken                                            extends TemplateToken
