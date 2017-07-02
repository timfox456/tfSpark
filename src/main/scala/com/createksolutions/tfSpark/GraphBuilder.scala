package com.createksolutions.tfSpark

import org.tensorflow.{Graph, Session, Tensor, Output, DataType}
import resource._

class GraphBuilder(g: Graph) {

  def div(x: Output, y: Output): Output = binaryOp("Div", x, y)
  def sub(x: Output, y: Output): Output = binaryOp("Sub", x, y)

  def resizeBilinear(images: Output, size: Output): Output =
    binaryOp("ResizeBilinear", images, size)

  def expandDims(input: Output, dim: Output): Output =
    binaryOp("ExpandDims", input, dim)

  def cast(value: Output, dtype: DataType): Output =
    g.opBuilder("Cast", "Cast")
      .addInput(value)
      .setAttr("DstT", dtype)
      .build()
      .output(0)

  def decodeJpeg(contents: Output, channels: Long): Output =
    g.opBuilder("DecodeJpeg", "DecodeJpeg")
      .addInput(contents)
      .setAttr("channels", channels)
      .build()
      .output(0)

  //def constant(name: String, value: AnyRef): Output =
  //  for (t <- managed(Tensor.create(value)))

  def constant(name: String, value: Any): Output = {
    val t = Tensor.create(value)
    g.opBuilder("Const", name)
      .setAttr("dtype", t.dataType())
      .setAttr("value", t)
      .build()
      .output(0)
  }

  private def binaryOp(`type`: String, in1: Output, in2: Output): Output =
    g.opBuilder(`type`, `type`).addInput(in1).addInput(in2).build().output(0)

}
