package com.mogtech.csvparser.routing

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.{ Flow, Framing, Source }
import akka.util.{ ByteString, Timeout }
import com.mogtech.csvparser.models.ResultData
import com.mogtech.csvparser.processing.FileProcessing
import com.mogtech.csvparser.utils.ConfigurationSettings
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ ExecutionContext, Future }

class HttpRoutes(settings: ConfigurationSettings)(implicit ec: ExecutionContext) extends LazyLogging {

  implicit val timeout: Timeout = Timeout(length = 90, TimeUnit.SECONDS)

  val dataProcessing: FileProcessing = FileProcessing(settings)

  val route: Route =
    extractRequestContext { ctx =>
      implicit val materializer: Materializer = ctx.materializer

      fileUpload("csv") {
        case (_, byteSource) =>
          val results = returnAsResultData(byteSource)

          onSuccess(results) {
            case "" => complete("All Data Processed Successfully")
            case err =>
              logger.error(err)
              complete(err)
          }
      }
    }

  def returnAsResultData(bs: Source[ByteString, Any])(implicit materializer: Materializer): Future[String] =
    bs.via { Framing.delimiter(ByteString(settings.newLine), maximumFrameLength = 1024, allowTruncation = true) }
      .via {
        Flow[ByteString].map((s: ByteString) => {
          val string = s.utf8String
          dataProcessing.processSplitsByLineBreak(string)
        })
      }
      .runFold(ResultData(Nil, None, None, balanced = true)) { (result, data) =>
        val newResult = dataProcessing.rejoinStringsWhereNeeded(result, data)
        newResult.goodData.foreach(gd => logger.info(gd))
        newResult.copy(goodData = None)
      }
      .map {
        case ResultData(errors, Some(current), _, false) => s"Unbalanced Result: $current\n${errors.mkString("\n")}"
        case ResultData(errors, _, _, _)                 => errors.mkString("\n")
      }
}
