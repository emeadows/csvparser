package com.mogtech.csvparser.processing

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ProcessDataSpec extends AnyWordSpec with Matchers {

  "splitDataOnDelimiter" should {
    "return NoData if empty" in {
      ProcessData.splitDataOnDelimiter(input = "", quoteType = "\"", delimiter = "\n") shouldBe Nil
    }

    "return a list with the size matching the number of new lines" in {
      val input = "a,b,c\nd,e,f"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n") should have size 2
    }

    "return a list with the size matching the number of new lines including empty lines" in {
      val input = "a,b,c\nd,e,f\n\n"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n") should have size 3
    }

    "return a list with the size matching the number of new lines excluding any newlines in double quotes" in {
      val input = "a,\"b\n\",c\nd,e,f\n"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n") should have size 2
    }

    "return a list with the size matching the number of new lines excluding any newlines in single quotes" in {
      val input = "a,\'b\n\',c\nd,e,f\n"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "\'", delimiter = "\n") should have size 2
    }

    "return a list with the size matching the number of new lines excluding any newlines in bespoke quotes of **" in {
      val input = "a,**b\n**,c\nd,e,f\n"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "**", delimiter = "\n") should have size 2
    }

    "return a list with the size matching the number of new lines excluding any newlines in bespoke quotes of special chars" in {
      val input = "a,\r\rb\n\r\r,c\nd,e,f\n"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "\r\r", delimiter = "\n") should have size 2
    }

    "return a list with the size matching the number of new lines excluding any newlines in bespoke quotes and delimiter of special chars" in {
      val input = "a,\'\"b\r\n\'\",c\r\nd,e,f\r\n"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "\'\"", delimiter = "\r\n") should have size 2
    }

    "process a line with delimiter keep any special characters or formatting" in {
      val input = "a,\"\n\r\n\",,700¥,e"
      ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n") shouldBe List(Right("a,\"\n\r\n\",,700¥,e"))
    }
  }
}
