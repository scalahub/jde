package jde.helpers

import jde.parser.Parser

trait TraitTimestamp {
  val timestampSource = scala.io.Source.fromFile("src/test/resources/timestamp.json").getLines.mkString
  val timestampProtocol = Parser.parse(timestampSource)
}
