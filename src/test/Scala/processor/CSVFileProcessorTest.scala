package processor

import org.scalatest.funsuite.AnyFunSuite
import java.io.File

class CSVFileProcessorTest extends AnyFunSuite with CSVFileProcessor {

  test("Get files list for input Directory Name") {
    val dirName = "sample"
    assert(getInputFiles(dirName).size > 0)
  }

  test("Get empty files list for input Directory Name") {
    val dirName = "."
    assert(getInputFiles(dirName).size == 0)
  }

  test("Process leader-1.csv File") {
    val file = new File("sample/leader-1.csv")
    val response = """Num of processed files: 1
               |Num of processed measurements: 4
               |Num of failed measurements: 1
               |
               |Sensors with highest avg humidity:
               |
               |sensor-id,min,avg,max
               |s1,98,98,98
               |s2,78,79,80
               |s3,NaN,NaN,NaN""".stripMargin
    val res = processCSVFile(file)
    assert(res.toString.stripMargin == response)
  }

}
