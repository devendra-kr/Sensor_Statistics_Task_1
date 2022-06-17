package model

import scala.collection.immutable.HashMap

case class OutputData(
                       sensors: Map[String, List[Option[Int]]] = Map(),
                       failedCount: Int = 0,
                       totalCount: Int = 0,
                       fileCount: Int = 0
                     ) {

  def +(rhs: Measurement): OutputData = {
    OutputData(
        if(sensors.contains(rhs.id))
          sensors + (rhs.id -> (sensors(rhs.id) :+ rhs.humidity))
        else sensors + (rhs.id -> List(rhs.humidity)),
      failedCount + (if (rhs.humidity.isDefined) 0 else 1),
      totalCount + 1,
      fileCount
    )
  }

  def |+|(rhs: OutputData): OutputData = {
    OutputData(
      addMap(sensors, rhs.sensors),
      failedCount + rhs.failedCount,
      totalCount + rhs.totalCount,
      fileCount + rhs.fileCount
    )
  }

  private def addMap(m1: Map[String, List[Option[Int]]], m2: Map[String, List[Option[Int]]])  = {
    m2 ++ m1.map{ case (k,v) => k -> (v ++ m2.getOrElse(k, None)) }
  }
}
