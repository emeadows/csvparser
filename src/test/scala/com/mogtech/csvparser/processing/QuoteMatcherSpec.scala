package com.mogtech.csvparser.processing

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class QuoteMatcherSpec extends AnyWordSpec with Matchers {

  "matchOnQuote" should {
    "return IsQuoteMatch is matches quote type" in {
      val result = QuoteMatcher.matchOnQuote(char = 'e', partialMatch = "Matching Valu", quoteType = "Matching Value")
      result shouldBe QuoteMatcher("Matching Value", IsQuoteMatch)
    }
    "return FailedMatch is does not quote type" in {
      val result = QuoteMatcher.matchOnQuote(char = 'F', partialMatch = "Matching ", quoteType = "Matching Value")
      result shouldBe QuoteMatcher("Matching F", FailedMatch)
    }
    "return Continue if partial match to quote type" in {
      val result = QuoteMatcher.matchOnQuote(char = 'V', partialMatch = "Matching ", quoteType = "Matching Value")
      result shouldBe QuoteMatcher("Matching V", Continue)
    }
  }
  "processMatchedData" should {
    "convert IsQuoteMatch to ProcessedMatch, unchanging result list, updating continue string and inverse boolean " in {
      val unchangingResult = List("Unchanged result string")
      val needClosingQuote = true
      val continueString   = "Continue String To Update with "
      val update           = "Quote"
      val result = QuoteMatcher(update, IsQuoteMatch)
        .processQuoteMatcher(unchangingResult, continueString, needClosingQuote)
      result shouldBe ProcessedMatch(unchangingResult, s"$continueString$update", "", !needClosingQuote)
    }

    "convert Continue to ProcessedMatch, unchanging result list, unchanged continue string inverse preserved" in {
      val unchangingResult   = List("Unchanged result string")
      val needsToBePreserved = false
      val continueString     = "No change to this"
      val update             = "Continuing As No Match or Fail"
      val result = QuoteMatcher(update, Continue)
        .processQuoteMatcher(unchangingResult, continueString, needsToBePreserved)
      result shouldBe ProcessedMatch(unchangingResult, continueString, update, needClosingQuote = needsToBePreserved)
    }
    "convert FailedMatch to ProcessedMatch, unchanging result list, update continue string and inverse preserved" in {
      val unchangingResult   = List("Unchanged result string")
      val needsToBePreserved = true
      val continueString     = "This is updated as the match has "
      val update             = "failed"
      val result = QuoteMatcher(update, FailedMatch)
        .processQuoteMatcher(unchangingResult, continueString, needsToBePreserved)
      result shouldBe ProcessedMatch(unchangingResult, s"$continueString$update", "", needClosingQuote = needsToBePreserved)
    }
  }
}
