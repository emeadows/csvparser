package com.mogtech.csvparser.processing

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DelimiterMatcherSpec extends AnyWordSpec with Matchers {

  "matchOnQuote" should {
    "return IsQuoteMatch is matches quote type" in {
      val result = DelimiterMatcher.matchOnQuote(char = 'e', partialMatch = "Matching Valu", quoteType = "Matching Value")
      result shouldBe DelimiterMatcher("Matching Value", IsQuoteMatch)
    }
    "return FailedMatch is does not quote type" in {
      val result = DelimiterMatcher.matchOnQuote(char = 'F', partialMatch = "Matching ", quoteType = "Matching Value")
      result shouldBe DelimiterMatcher("Matching F", FailedMatch)
    }
    "return Continue if starts match to quote type" in {
      val result = DelimiterMatcher.matchOnQuote(char = 'M', partialMatch = "", quoteType = "Matching Value")
      result shouldBe DelimiterMatcher("M", Continue)
    }
    "return Continue if partial match to quote type" in {
      val result = DelimiterMatcher.matchOnQuote(char = 'V', partialMatch = "Matching ", quoteType = "Matching Value")
      result shouldBe DelimiterMatcher("Matching V", Continue)
    }
  }

  "matchOnQuoteOrDelimiter" should {
    "return IsQuoteMatch is matches quote type" in {
      val result = DelimiterMatcher.matchOnQuoteOrDelimiter(char = 'e', partialMatch = "Quot", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("Quote", IsQuoteMatch)
    }
    "return IsDelimiterMatch is matches delimiter type" in {
      val result =
        DelimiterMatcher.matchOnQuoteOrDelimiter(char = 'r', partialMatch = "Delimite", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("Delimiter", IsDelimiterMatch)
    }
    "return FailedMatch if match fails on quote type" in {
      val result = DelimiterMatcher.matchOnQuoteOrDelimiter(char = 'x', partialMatch = "Quo", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("Quox", FailedMatch)
    }
    "return FailedMatch if match fails on delimiter type" in {
      val result = DelimiterMatcher.matchOnQuoteOrDelimiter(char = 'x', partialMatch = "Del", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("Delx", FailedMatch)
    }
    "return Continue if starts to match quote type" in {
      val result = DelimiterMatcher.matchOnQuoteOrDelimiter(char = 'Q', partialMatch = "", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("Q", Continue)
    }
    "return Continue is starts to match quote type" in {
      val result = DelimiterMatcher.matchOnQuoteOrDelimiter(char = 'D', partialMatch = "", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("D", Continue)
    }
    "return Continue if continues to match quote type" in {
      val result = DelimiterMatcher.matchOnQuoteOrDelimiter(char = 't', partialMatch = "Quo", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("Quot", Continue)
    }
    "return Continue if continues to match delimiter type" in {
      val result = DelimiterMatcher.matchOnQuoteOrDelimiter(char = 'i', partialMatch = "Del", quoteType = "Quote", delimiter = "Delimiter")
      result shouldBe DelimiterMatcher("Deli", Continue)
    }
  }

  "processMatchedData" should {
    "convert IsQuoteMatch to ProcessedMatch, unchanging result list, updating continue string and inverse boolean " in {
      val unchangingResult = List(Right("Unchanged result string"))
      val needClosingQuote = true
      val continueString   = "Continue String To Update with "
      val update           = "Quote"
      val result = DelimiterMatcher(update, IsQuoteMatch)
        .processDelimiterMatcher(unchangingResult, continueString, needClosingQuote)
      result shouldBe ProcessedMatch(unchangingResult, s"$continueString$update", "", !needClosingQuote)
    }
    "convert IsDelimiterMatch to ProcessedMatch, update result list, clear continue string and inverse is false" in {
      val resultString       = Right("Unchanged result string")
      val thisCanNeverBeTrue = false
      val continueString     = "New String will update without delimiter"
      val result = DelimiterMatcher("Delimiter", IsDelimiterMatch)
        .processDelimiterMatcher(List(resultString), continueString, thisCanNeverBeTrue)
      result shouldBe ProcessedMatch(List(resultString, Right(continueString)), "", "", needClosingQuote = false)
    }
    "convert Continue to ProcessedMatch, unchanging result list, unchanged continue string inverse preserved" in {
      val unchangingResult   = List(Right("Unchanged result string"))
      val needsToBePreserved = false
      val continueString     = "No change to this"
      val update             = "Continuing As No Match or Fail"
      val result = DelimiterMatcher(update, Continue)
        .processDelimiterMatcher(unchangingResult, continueString, needsToBePreserved)
      result shouldBe ProcessedMatch(unchangingResult, continueString, update, needClosingQuote = needsToBePreserved)
    }
    "convert FailedMatch to ProcessedMatch, unchanging result list, update continue string and inverse preserved" in {
      val unchangingResult   = List(Right("Unchanged result string"))
      val needsToBePreserved = true
      val continueString     = "This is updated as the match has "
      val update             = "failed"
      val result = DelimiterMatcher(update, FailedMatch)
        .processDelimiterMatcher(unchangingResult, continueString, needsToBePreserved)
      result shouldBe ProcessedMatch(unchangingResult, s"$continueString$update", "", needClosingQuote = needsToBePreserved)
    }
  }
}
