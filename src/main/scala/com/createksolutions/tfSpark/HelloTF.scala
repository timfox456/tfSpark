package com.createksolutions.tfSpark

import org.tensorflow.{Graph, Session, Tensor}

import scala.collection.JavaConverters._

object HelloTF {
  private val valueString = "Hello Tensorflow!"
  def main(args: Array[String]): Unit = {

    val graph = new Graph()
    val t = Tensor.create(valueString.getBytes("UTF-8"))
    graph
      .opBuilder("Const", "MyConst")
      .setAttr("dtype", t.dataType())
      .setAttr("value", t)
      .build();

    val session: Session = new Session(graph)
    val output = session.runner().fetch("MyConst").run().get(0)
    println(new String(output.bytesValue(), "UTF-8"))
  }
}
