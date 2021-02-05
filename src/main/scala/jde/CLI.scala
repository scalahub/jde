package jde

import jde.compiler.TxBuilder
import jde.parser.Parser._
import kiosk.ergo.usingSource
import kiosk.explorer.Explorer
import play.api.libs.json.Json

import scala.io.Source

object CLI {
  def main(args: Array[String]): Unit = {
    if (args.size != 1) println("Usage java -jar <jarFile> <script.json>")
    else {
      val script = args(0)
      val source = usingSource(Source.fromFile(script)) { src =>
        src.mkString
      }
      val compileResult = new TxBuilder(new Explorer).compile(parse(source))
      println(Json.prettyPrint(Json.toJson(compileResult)))
    }
  }
}
