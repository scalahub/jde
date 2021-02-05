package jde.helpers

import jde.parser.Parser

trait TraitTokenFilter {
  val tokenFilterSource = scala.io.Source.fromFile("src/test/resources/token-filter.json").getLines.mkString
  val tokenFilterProtocol = Parser.parse(tokenFilterSource)
}
