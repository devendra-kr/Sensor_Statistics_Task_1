
import processor.DataProcessor

object Main {

  def main(args: Array[String]): Unit = {
    val processor = new DataProcessor()
    val result = processor.run(args.headOption.getOrElse("sample"))
    println(result)
  }

}
