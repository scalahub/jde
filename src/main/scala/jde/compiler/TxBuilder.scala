package jde.compiler

import kiosk.explorer.Explorer
import jde.compiler.model._
import kiosk.ergo

class TxBuilder(explorer: Explorer) {
  def compile(protocol: Protocol) = {
    implicit val dictionary = new Dictionary(explorer.getHeight)
    // Step 1. validate that constants are properly encoded
    optSeq(protocol.constants).map(_.getValue)
    // Step 2. load declarations (also does semantic validation)
    (new OffChainLoader).load(protocol)
    // Step 3. load on-chain declarations
    new OnChainLoader(explorer).load(protocol)
    // Step 4. validate post-conditions
    optSeq(protocol.postConditions).foreach(_.validate)
    // Step 5. build outputs
    val outputs = (new Builder).buildOutputs(protocol)
    // Step 6. compute values to return
    val returns = optSeq(protocol.returns).map { name =>
      val declaration: Declaration = dictionary.getDeclaration(name)
      val values: Seq[ergo.KioskType[_]] = declaration.getValue.seq
      ReturnedValue(name, declaration.`type`, values)
    }
    // Return final result
    CompileResult(
      dictionary.getDataInputBoxIds,
      dictionary.getInputBoxIds,
      dictionary.getInputNanoErgs,
      dictionary.getInputTokens,
      outputs,
      protocol.fee,
      returns
    )
  }
}
