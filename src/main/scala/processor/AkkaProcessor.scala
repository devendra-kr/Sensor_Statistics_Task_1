package processor

import java.io.File

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import config.Constants._
import model.OutputData

import scala.concurrent.Future

class AkkaProcessor extends CSVFileProcessor {

  def run(dirName: String): Future[OutputData] = {
    implicit val system = ActorSystem(actorName)
    implicit val ec = system.dispatcher

    val source = Source(getInputFiles(dirName))
    source.via(Flow[File].mapAsync(parallelism = parallelCount) (file
      => {
      println("file " + file)
      Future(processCSVFile(file, null))
    })).runWith(Sink.fold(OutputData())(_ |+| _))
  }
}
