
import processor.SequentialProcessor
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]) = {

    val processor = new SequentialProcessor()
    val result = processor.run(args.headOption.getOrElse("sample"))
    val sensors = processor.calculates(result.sensors).flatten
    val sensorsWithNaNHumidity = result.sensors.keySet.diff(sensors.map(_.id).toSet)
    val sensorsWithSorted = sensors.toList.sortBy(-_.avg)

    println(s"Num of processed files: ${result.fileCount}")
    println(s"Num of processed measurements: ${result.totalCount}")
    println(s"Num of failed measurements: ${result.failedCount}\n")

    println(s"Sensors with highest avg humidity:\n")
    println(s"sensor-id,min,avg,max")
    sensorsWithSorted.map(sensor => {
      println(s"${sensor.id},${sensor.min},${sensor.avg},${sensor.max}")
    })
    sensorsWithNaNHumidity.map(sensorId => {
      println(s"$sensorId,NaN,NaN,NaN")
    })
    println("")
  }
}
