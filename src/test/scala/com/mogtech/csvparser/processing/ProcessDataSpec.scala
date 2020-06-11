package com.mogtech.csvparser.processing

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ProcessDataSpec extends AnyWordSpec with Matchers {

  "splitByQuotes" should {
    "return empty list if empty" in {
      val result = ProcessData.splitByQuotes(input = "", quoteType = "\"")
      result shouldBe SplitByQuotes(List(), "", balanced = true)
    }

    "return a list with the size one if no quotes" in {
      val input  = "a,b,c"
      val result = ProcessData.splitByQuotes(input = input, quoteType = "\"")
      result shouldBe SplitByQuotes(List("a,b,c"), input, balanced = true)
    }

    "return a list with the size matching the number of double quotes" in {
      val input  = "a,\"b"
      val result = ProcessData.splitByQuotes(input = input, quoteType = "\"")
      result shouldBe SplitByQuotes(List("a,", "b"), input, balanced = false)
    }

    "return a list with the size matching the number of single quotes" in {
      val input  = "a,\'b"
      val result = ProcessData.splitByQuotes(input = input, quoteType = "\'")
      result shouldBe SplitByQuotes(List("a,", "b"), input, balanced = false)
    }

    "return a list with the size matching the number of bespoke quotes of **" in {
      val input  = "a,**b"
      val result = ProcessData.splitByQuotes(input = input, quoteType = "**")
      result shouldBe SplitByQuotes(List("a,", "b"), input, balanced = false)
    }

    "return a list with the size matching the number of in bespoke quotes of special chars" in {
      val input  = "a,\r\rb\n\r\r,c\nd,e,f\n"
      val result = ProcessData.splitByQuotes(input = input, quoteType = "\r\r")
      result shouldBe SplitByQuotes(List("a,", "b\n", ",c\nd,e,f\n"), input, balanced = true)
    }

    "process a line with delimiter keep any special characters or formatting" in {
      val input  = "a,\"\n\r\n\",,700¥,e"
      val result = ProcessData.splitByQuotes(input = input, quoteType = "\"")
      result shouldBe SplitByQuotes(List("a,", "\n\r\n", ",,700¥,e"), input, balanced = true)
    }
  }

  "splitDataOnDelimiter" should {

    "return a list with the size matching the number of new lines and rest of data separately" in {
      val input  = "a,b,c\nd,e,f"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n")
      result shouldBe List("a,b,c", "d,e,f")
    }

    "return a list with the size matching the number of new lines including empty lines" in {
      val input  = "a,b,c\nd,e,f\n\n"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n")
      result shouldBe List("a,b,c", "d,e,f", "", "")
    }

    "return a list with the size matching the number of new lines excluding any newlines in double quotes" in {
      val input  = "a,\"b\n\",c\nd,e,f\n"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n")
      result shouldBe List("a,\"b\n\",c", "d,e,f", "")
    }

    "return a list with the size matching the number of new lines excluding any newlines in single quotes" in {
      val input  = "a,\'b\n\',c\nd,e,f\n"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "\'", delimiter = "\n")
      result shouldBe List("a,\'b\n\',c", "d,e,f", "")
    }

    "return a list with the size matching the number of new lines excluding any newlines in bespoke quotes of **" in {
      val input  = "a,**b\n**,c\nd,e,f\n"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "**", delimiter = "\n")
      result shouldBe List("a,**b\n**,c", "d,e,f", "")
    }

    "return a list with the size matching the number of new lines excluding any newlines in bespoke quotes of special chars" in {
      val input  = "a,\r\rb\n\r\r,c\nd,e,f\n"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "\r\r", delimiter = "\n")
      result shouldBe List("a,\r\rb\n\r\r,c", "d,e,f", "")
    }

    "return a list with the size matching the number of new lines excluding any newlines in bespoke quotes and delimiter of special chars" in {
      val input  = "a,\'\"b\r\n\'\",c\r\nd,e,f\r\n"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "\'\"", delimiter = "\r\n")
      result shouldBe List("a,\'\"b\r\n\'\",c", "d,e,f", "")
    }

    "process a line with delimiter keep any special characters or formatting" in {
      val input  = "a,\"\n\r\n\",,700¥,e"
      val result = ProcessData.splitDataOnDelimiter(input = input, quoteType = "\"", delimiter = "\n")
      result shouldBe List("a,\"\n\r\n\",,700¥,e")
    }
  }
}
