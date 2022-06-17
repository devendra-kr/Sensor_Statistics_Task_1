package processor

import model.OutputData

class DataProcessor extends CSVFileProcessor {

  def run(directoryName: String): OutputData = getInputFiles(directoryName)
    .map(file => processCSVFile(file)).fold(OutputData())(_ combine _)

}