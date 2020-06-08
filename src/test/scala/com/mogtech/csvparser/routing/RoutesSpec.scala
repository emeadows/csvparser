package com.mogtech.csvparser.routing

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.Multipart.FormData
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.{ RouteTest, RouteTestTimeout, ScalatestRouteTest }
import com.mogtech.csvparser.utils.ConfigurationSettings
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration.FiniteDuration

class RoutesSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with RouteTest with TestDataSets {

  implicit val timeout: RouteTestTimeout = RouteTestTimeout(new FiniteDuration(5, TimeUnit.SECONDS))

  val conf: ConfigurationSettings = ConfigurationSettings.conf
  val httpRoutes = new HttpRoutes(conf)

  "Routes POST" should {
    "basic test file" in {
      val uploadData = testFile
      val multipartForm: FormData.Strict = multipartFormData(uploadData)

      Post("/", multipartForm) ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "All Data Processed Successfully"
      }
    }

    "test file with line breaks and random quotes" in {
      val uploadData = testFileWithLineBreaksAndRandomQuotes
      val multipartForm: FormData.Strict = multipartFormData(uploadData)

      Post("/", multipartForm) ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "The following data could not be processed:\n\nUnbalanced Quotes\n\tKZT\t USD\t0.00\"249\t0.00242478\t#VALUE!\t\tKZT USD\n"
      }
    }
    "testFileWithLineRandomQuotes" in {
      val uploadData = testFileWithLineRandomQuotes
      val multipartForm: FormData.Strict = multipartFormData(uploadData)

      Post("/", multipartForm) ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "All Data Processed Successfully"
      }
    }
    "germanTestFile" in {
      val uploadData = germanTestFile
      val multipartForm: FormData.Strict = multipartFormData(uploadData)

      Post("/", multipartForm) ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "All Data Processed Successfully"
      }
    }
    "germanTestFileInDifferentEncoding" in {
      val uploadData = germanTestFileInDifferentEncoding
      val multipartForm: FormData.Strict = multipartFormData(uploadData)

      Post("/", multipartForm) ~> httpRoutes.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "All Data Processed Successfully"
      }
    }
  }

  def multipartFormData(uploadData: UploadData): FormData.Strict = {
    val contentType: ContentType.WithCharset = MediaTypes.`text/plain` withCharset uploadData.encoding
    Multipart.FormData(
      Multipart.FormData.BodyPart
        .Strict("csv", HttpEntity(contentType, uploadData.data), Map("filename" -> uploadData.filename))
    )
  }

}
