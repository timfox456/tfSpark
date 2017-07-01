name := "tfSpark"

version := "1.0"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.10.6") 

libraryDependencies ++= Seq(
  "org.tensorflow" % "tensorflow" % "1.2.0",
  "org.apache.spark" %% "spark-core" % "2.1.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.1.0" % "provided",
  "com.jsuereth" %% "scala-arm" % "1.4",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

organization := "com.createksolutions"

licenses += "Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")


