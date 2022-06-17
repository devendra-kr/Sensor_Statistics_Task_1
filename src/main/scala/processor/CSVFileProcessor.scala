package processor

import java.io.{File, IOException}

import com.github.tototoshi.csv.CSVReader
import model.{Measurement, OutputData}

trait CSVFileProcessor {

  def getInputFiles(dirName: String): Seq[File] = {
    val dir = new File(dirName)
    if (!dir.isDirectory) throw new IllegalArgumentException("Invalid Directory")
    dir.listFiles {
      (_, name) => name.endsWith(".csv")
    }.toIndexedSeq
  }

  def processCSVFile(file: File, outputData: OutputData = OutputData()): OutputData = {

    val reader = CSVReader.open(file)

    val fileOutputData = reader.toStream.tail.map {
      case id :: "NaN" :: Nil => Measurement(id, None)
      case id :: humidity :: Nil => Measurement(id, Option(humidity.toInt))
      case _ => throw new IOException(s"Invalid format of file ${file.getName}")
    }
      .foldLeft(outputData)(_ add _)

    val result = fileOutputData.copy(fileCount = fileOutputData.fileCount + 1)
    reader.close()
    result
  }

}
