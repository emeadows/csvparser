package com.mogtech.csvparser.processing

import com.mogtech.csvparser.models.{ ResultData, SplitByQuotes }
import com.mogtech.csvparser.utils.ConfigurationSettings
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FileProcessingSpec extends AnyWordSpec with Matchers {

  val settings: ConfigurationSettings = ConfigurationSettings(quotedType = "\"", newLine = "\n", delimiter = ",")

  val fileProcessing: FileProcessing = FileProcessing(settings)
  "rejoinStringsWhereNeeded" should {

    "handle data till end of actual line" in {

      val emptyResultData = ResultData(Nil, None, None, balanced = true)
      val line1           = "something,\"something with a line break"
      val splitBtQuotes1  = Right(SplitByQuotes(List("something,", "something with a line break"), line1, balanced = false))
      val updatedLine1    = "something,\"something with a line break\n"
      val resultData1     = ResultData(Nil, Some(updatedLine1), None, balanced = false)
      fileProcessing.rejoinStringsWhereNeeded(emptyResultData, splitBtQuotes1) shouldBe resultData1

      val line2          = "\",\""
      val splitByQuotes2 = Right(SplitByQuotes(List("", ","), line2, balanced = true))
      val updatedLine2   = "something,\"something with a line break\n\",\"\n"
      val resultData2    = ResultData(Nil, Some(updatedLine2), None, balanced = false)
      fileProcessing.rejoinStringsWhereNeeded(resultData1, splitByQuotes2) shouldBe resultData2

      val line3          = "something with "
      val splitByQuotes3 = Right(SplitByQuotes(List("something with"), line3, balanced = true))
      val updatedLine3   = "something,\"something with a line break\n\",\"\nsomething with \n"
      val resultData3    = ResultData(Nil, Some(updatedLine3), None, balanced = false)
      fileProcessing.rejoinStringsWhereNeeded(resultData2, splitByQuotes3) shouldBe resultData3

      val line4          = "many line breaks"
      val splitByQuotes4 = Right(SplitByQuotes(List("many line breaks"), line4, balanced = true))
      val updatedLine4   = "something,\"something with a line break\n\",\"\nsomething with \nmany line breaks\n"
      val resultData4    = ResultData(Nil, Some(updatedLine4), None, balanced = false)
      fileProcessing.rejoinStringsWhereNeeded(resultData3, splitByQuotes4) shouldBe resultData4

      val line5          = ",more things,\"something plane in quote\" "
      val splitByQuotes5 = Right(SplitByQuotes(List("", ",more things,", "something plane in quote", " "), line5, balanced = false))
      val fullLine =
        "something,\"something with a line break\n\",\"\nsomething with \nmany line breaks\n,more things,\"something plane in quote\" "
      val fullLineResult = ResultData(Nil, None, Some(fullLine), balanced = true)
      fileProcessing.rejoinStringsWhereNeeded(resultData4, splitByQuotes5) shouldBe fullLineResult
    }

  }
}
