package model

import scala.util.Properties

case class OutputData(
                       sensors: Map[String, List[Option[Int]]] = Map(),
                       failedCount: Int = 0,
                       totalCount: Int = 0,
                       fileCount: Int = 0
                     ) {

  def add(rhs: Measurement): OutputData = {
    OutputData(
      if (sensors.contains(rhs.id))
        sensors + (rhs.id -> (sensors(rhs.id) :+ rhs.humidity))
      else sensors + (rhs.id -> List(rhs.humidity)),
      failedCount + (if (rhs.humidity.isDefined) 0 else 1),
      totalCount + 1,
      fileCount
    )
  }

  def combine(rhs: OutputData): OutputData = {
    OutputData(
      addMap(sensors, rhs.sensors),
      failedCount + rhs.failedCount,
      totalCount + rhs.totalCount,
      fileCount + rhs.fileCount
    )
  }

  private def addMap(m1: Map[String, List[Option[Int]]], m2: Map[String, List[Option[Int]]]) = {
    m2 ++ m1.map { case (k, v) => k -> (v ++ m2.getOrElse(k, None)) }
  }

  private def aggregator(m: Map[String, List[Option[Int]]]): Iterable[Option[Aggregation]] = {
    m.map {
      case (key, value) => {
        val flatList = value.flatten
        if (flatList.nonEmpty) {
          val min = flatList.reduceLeft(_ min _)
          val avg = flatList.sum / flatList.length
          val max = flatList.reduceLeft(_ max _)
          Some(Aggregation(key, min, avg, max))
        } else None
      }
    }
  }

  override def toString: String = {
    val sensorsAggregation = aggregator(sensors).flatten
    val sensorsWithNaN = sensors.keySet.diff(sensorsAggregation.map(_.id).toSet)

    s"""Num of processed files: $fileCount
       |Num of processed measurements: $totalCount
       |Num of failed measurements: $failedCount
       |
       |Sensors with highest avg humidity:
       |
       |sensor-id,min,avg,max
       |${
      sensorsAggregation.toSeq.sortBy(-_.avg).map { sensor => {
        s"${sensor.id},${sensor.min},${sensor.avg},${sensor.max}"
      }
      }.mkString(Properties.lineSeparator)
    }
       |${
      sensorsWithNaN.map { sensorId => {
        s"$sensorId,NaN,NaN,NaN"
      }
      }.mkString(Properties.lineSeparator)
    }""".stripMargin
  }
}
