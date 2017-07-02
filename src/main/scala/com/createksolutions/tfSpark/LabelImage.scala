package com.createksolutions.tfSpark

import java.io.IOException
import java.io.PrintStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.tensorflow.{Graph, Session, Tensor, Output, DataType, TensorFlow}
import scala.collection

//remove if not needed
import resource._
import scala.collection.JavaConversions._

/** Sample use of the TensorFlow Java API to label images using a pre-trained model. */
object LabelImage {

  private def printUsage(s: PrintStream): Unit = {
    val url =
      "https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip"
    s.println(
      "Scala program that uses a pre-trained Inception model (http://arxiv.org/abs/1512.00567)")
    s.println("to label JPEG images.")
    s.println("TensorFlow version: " + TensorFlow.version())
    s.println()
    s.println("Usage: label_image <model dir> <image file>")
    s.println()
    s.println("Where:")
    s.println(
      "<model dir> is a directory containing the unzipped contents of the inception model")
    s.println("            (from " + url + ")")
    s.println("<image file> is the path to a JPEG image file")
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      printUsage(System.err)
      System.exit(1)
    }
    val modelDir: String = args(0)
    val imageFile: String = args(1)
    val graphDef: Array[Byte] = readAllBytesOrExit(
      Paths.get(modelDir, "tensorflow_inception_graph.pb"))
    val labels: List[String] = readAllLinesOrExit(
      Paths.get(modelDir, "imagenet_comp_graph_label_strings.txt"))
    val imageBytes: Array[Byte] = readAllBytesOrExit(Paths.get(imageFile))

    val image = constructAndExecuteGraphToNormalizeImage(imageBytes)
    val labelProbabilities: Array[Float] =
      executeInceptionGraph(graphDef, image)
    val bestLabelIdx: Int = maxIndex(labelProbabilities)
    val probability: Float = labelProbabilities(bestLabelIdx) * 100f
    println("BEST MATCH: $bestLabelIdx ($probability%2.2f likely)")
  }

  private def constructAndExecuteGraphToNormalizeImage(
      imageBytes: Array[Byte]): Tensor = {
    val g = new Graph()
    val b: GraphBuilder = new GraphBuilder(g)
    // Some constants specific to the pre-trained model at:
    // https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip
    //
    // - The model was trained with images scaled to 224x224 pixels.
    // - The colors, represented as R, G, B in 1-byte each were converted to
    //   float using (value - Mean)/Scale.
    val H: Int = 224
    val W: Int = 224
    val mean: Float = 117f
    val scale: Float = 1f

    // Since the graph is being constructed once per execution here, we can use a constant for the
    // input image. If the graph were to be re-used for multiple input images, a placeholder would
    // have been more appropriate.
    val input: Output = b.constant("input", imageBytes)
    val output: Output = b.div(
      b.sub(
        b.resizeBilinear(
          b.expandDims(b.cast(b.decodeJpeg(input, 3), DataType.FLOAT),
                       b.constant("make_batch", 0)),
          b.constant("size", Array(H, W))),
        b.constant("mean", mean)
      ),
      b.constant("scale", scale)
    )
    val s = new Session(g)
    s.runner().fetch(output.op().name()).run().get(0)
  }
// Some constants specific to the pre-trained model at:
// https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip
//
// - The model was trained with images scaled to 224x224 pixels.
// - The colors, represented as R, G, B in 1-byte each were converted to
// Since the graph is being constructed once per execution here, we can use a constant for the
// input image. If the graph were to be re-used for multiple input images, a placeholder would
// Some constants specific to the pre-trained model at:
// https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip
//
// - The model was trained with images scaled to 224x224 pixels.
// - The colors, represented as R, G, B in 1-byte each were converted to
// Since the graph is being constructed once per execution here, we can use a constant for the
// input image. If the graph were to be re-used for multiple input images, a placeholder would

  private def executeInceptionGraph(graphDef: Array[Byte],
                                    image: Tensor): Array[Float] = {
    val g = new Graph();
    g.importGraphDef(graphDef)
    val s = new Session(g)
    val result: Tensor =
      s.runner().feed("input", image).fetch("output").run().get(0)
    val rshape: Array[Long] = result.shape()
    if (result.numDimensions() != 2 || rshape(0) != 1) {
      throw new RuntimeException(
        String.format(
          "Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
          rshape.mkString(", ")))
    }
    val nlabels: Int = rshape(1).toInt
    result.copyTo(Array.ofDim[Float](1, nlabels))(0)
  }

  private def maxIndex(probabilities: Array[Float]): Int = {
    var best: Int = 0
    for (i <- 1 until probabilities.length
         if probabilities(i) > probabilities(best)) {
      best = i
    }
    best
  }

  private def readAllBytesOrExit(path: Path): Array[Byte] = {
    try Files.readAllBytes(path)
    catch {
      case e: IOException => {
        System.err.println("Failed to read [" + path + "]: " + e.getMessage)
        System.exit(1)
      }

    }
    null
  }

  private def readAllLinesOrExit(path: Path): List[String] = {
    try Files.readAllLines(path, Charset.forName("UTF-8"))
    catch {
      case e: IOException => {
        System.err.println("Failed to read [" + path + "]: " + e.getMessage)
        System.exit(0)
      }

    }
    null
  }
}
