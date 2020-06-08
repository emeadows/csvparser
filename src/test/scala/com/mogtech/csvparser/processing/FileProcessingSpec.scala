package com.mogtech.csvparser.processing

import com.mogtech.csvparser.models.ErrorType
import com.mogtech.csvparser.utils.ConfigurationSettings
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FileProcessingSpec extends AnyWordSpec with Matchers {

  val settings: ConfigurationSettings = ConfigurationSettings(quotedType = "\"", newLine = "\n", delimiter = ",")

  val fileProcessing: FileProcessing = FileProcessing(settings)
  "processInput" should {
    "return list of lists based on delimiters" in {
      val input           = """Ccy,To,Ccy,From,Rate,Online,,,Curremcy,Pair
                    |NOK,,USD,0.106888996,0.106084037,1.007587931
                    |KZT,,USD,0.00249,0.00242478,1.026897286,,KZT,USD
                    |HKD,,USD,0.12903,0.129030038,0.999999704
                    |KZT,,GBP,0.00198,0.00197295,1.003573329,,KZT,GBP
                    |SEK,,USD,0.108858941,0.107994471,1.008004761""".stripMargin
      val processedResult = fileProcessing.processInputString(input)
      processedResult.goodData should have size 6
      processedResult.goodData.head shouldBe List("Ccy", "To", "Ccy", "From", "Rate", "Online", "", "", "Curremcy", "Pair")
      processedResult.goodData(4) shouldBe List("KZT", "", "GBP", "0.00198", "0.00197295", "1.003573329", "", "KZT", "GBP")
      processedResult.goodData.last shouldBe List("SEK", "", "USD", "0.108858941", "0.107994471", "1.008004761")
    }
    "return list and cope with formatting" in {
      val input           = """HUF," USD
                              |
                              |",0.003289907,0.003261815,1.00861226,,
                              |KZT, USD,0.00"249,0.00242478,#VALUE!,,KZT USD""".stripMargin
      val processedResult = fileProcessing.processInputString(input)
      processedResult.goodData should have size 1
      processedResult.errors should have size 1
      processedResult.goodData.head shouldBe List("HUF", "\" USD\n\n\"", "0.003289907", "0.003261815", "1.00861226", "")
      processedResult.errors.head shouldBe ErrorType("Unbalanced Quotes", "KZT, USD,0.00\"249,0.00242478,#VALUE!,,KZT USD")
    }
  }
}
