package model

case class Measurement(id: String, humidity: Option[Int])

case class Aggregation(id: String, min: Int, avg: Int, max: Int)