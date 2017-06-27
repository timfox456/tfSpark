import org.tensorflow.{Graph, Session, Tensor}

import scala.collection.JavaConverters._
import org.scalatest._


class HelloTFTest extends FlatSpec {
  private val valueString = "Hello Tensorflow!"

  "HelloTFTest" should "Correctly Print Out String"

    val graph = new Graph()
    val t = Tensor.create(valueString.getBytes("UTF-8"))
    graph.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();

    val session: Session = new Session(graph)
    val output = session.runner().fetch("MyConst").run().get(0)
    println(new String(output.bytesValue(), "UTF-8"))
  
}

