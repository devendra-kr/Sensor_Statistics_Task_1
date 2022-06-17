package processor

import java.io.File

import org.scalatest.funsuite.AnyFunSuite

class DataProcessorTest extends AnyFunSuite {

  test("Process All Files from given Directory") {
    val dirName = "sample"
    val response = """Num of processed files: 2
                     |Num of processed measurements: 7
                     |Num of failed measurements: 2
                     |
                     |Sensors with highest avg humidity:
                     |
                     |sensor-id,min,avg,max
                     |s2,78,82,88
                     |s1,10,54,98
                     |s3,NaN,NaN,NaN""".stripMargin
    val res = new DataProcessor().run(dirName)
    assert(res.toString.stripMargin == response)
  }

  test("Empty result for non *.csv files") {
    val dirName = "."
    val res = new DataProcessor().run(dirName)
    assert(res.toString.contains("Num of processed files: 0"))
  }
}
