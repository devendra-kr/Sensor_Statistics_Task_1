package processor

import model.{Aggregation, OutputData}

import scala.math.Ordering.Implicits.seqOrdering

class SequentialProcessor extends CSVFileProcessor{

  def run(directoryName: String): OutputData = getInputFiles(directoryName)
    .map(file => processCSVFile(file)).fold(OutputData())(_ |+| _)

  /*
  min/avg/max
  s2,78,82,88
s1,10,54,98
s3,NaN,NaN,NaN
Map(s2 -> List(Some(88), Some(80), Some(78)), s3 -> List(None), s1 -> List(Some(10), None, Some(98))),2,7,2)

  */

  def calculates(m: Map[String, List[Option[Int]]]) = {
    m.map {
      case (key, value) => {
        val flatList = value.flatten
        if(flatList.nonEmpty) {
          val min = flatList.reduceLeft(_ min _)
          val avg = flatList.sum / flatList.length
          val max = flatList.reduceLeft(_ max _)
          Some(Aggregation(key, min, avg, max))
        } else None
      }
    }
  }


}