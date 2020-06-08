package com.mogtech.csvparser.routing

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.server.Directives.{ complete, _ }
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.Timeout
import com.mogtech.csvparser.processing.FileProcessing
import com.mogtech.csvparser.utils.ConfigurationSettings

class HttpRoutes(settings: ConfigurationSettings) {

  implicit val timeout: Timeout = Timeout(length = 90, TimeUnit.SECONDS)

  val dataProcessing: FileProcessing = FileProcessing(settings)

  val route: Route =
    extractRequestContext { ctx =>
      implicit val materializer: Materializer = ctx.materializer

      fileUpload("csv") {
        case (_, byteSource) =>
          val errors = byteSource
            .map(bs => bs.utf8String)
            .map(dataProcessing.processInputString)
            .map { result =>
              println(result.successfullyProcessed)
              result
            }
            .runFold("")((_, result) => result.errorsThrown)

          onSuccess(errors) { err =>
            complete(err)
          }
      }
    }

}