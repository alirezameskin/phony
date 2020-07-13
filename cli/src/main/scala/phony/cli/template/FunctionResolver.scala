package phony.cli.template

import phony.cli.template.parser.FunctionCall

trait FunctionResolver[F[_]] {
  def resolve: PartialFunction[FunctionCall, F[String]]
}
