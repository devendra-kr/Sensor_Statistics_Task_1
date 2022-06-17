name := "Sensor_Statistics_Task"

version := "0.1"

scalaVersion := "2.13.8"

val AkkaVersion = "2.6.8"

libraryDependencies ++= Seq(
  "com.github.tototoshi" %% "scala-csv" % "1.3.6",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "org.scalatest" %% "scalatest" % "3.1.1" % "test"
)
