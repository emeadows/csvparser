package com.mogtech.csvparser.processing

import com.mogtech.csvparser.models.ErrorType

case class DelimiterMatcher(update: String, matched: MatchedType) {

  def processDelimiterMatcher(result: List[Either[ErrorType, String]], current: String, needClosingQuote: Boolean): ProcessedMatch =
    matched match {
      case IsQuoteMatch =>
        ProcessedMatch(result, current = s"$current$update", partialMatch = "", needClosingQuote = !needClosingQuote)
      case IsDelimiterMatch =>
        ProcessedMatch(result.appended(Right(current)), current = "", partialMatch = "", needClosingQuote = needClosingQuote)
      case Continue =>
        ProcessedMatch(result, current = current, partialMatch = update, needClosingQuote = needClosingQuote)
      case FailedMatch =>
        ProcessedMatch(result, current = s"$current$update", partialMatch = "", needClosingQuote = needClosingQuote)
    }
}

case class ProcessedMatch(result: List[Either[ErrorType, String]], current: String, partialMatch: String, needClosingQuote: Boolean)

object DelimiterMatcher {

  def matchOnQuote(char: Char, partialMatch: String, quoteType: String): DelimiterMatcher =
    s"$partialMatch$char" match {
      case `quoteType`                                      => DelimiterMatcher(quoteType, IsQuoteMatch)
      case str if quoteType.substring(0, str.length) == str => DelimiterMatcher(str, Continue)
      case str                                              => DelimiterMatcher(str, FailedMatch)
    }

  def matchOnQuoteOrDelimiter(char: Char, partialMatch: String, quoteType: String, delimiter: String): DelimiterMatcher =
    s"$partialMatch$char" match {
      case `quoteType`                                      => DelimiterMatcher(quoteType, IsQuoteMatch)
      case `delimiter`                                      => DelimiterMatcher(delimiter, IsDelimiterMatch)
      case str if quoteType.substring(0, str.length) == str => DelimiterMatcher(str, Continue)
      case str if delimiter.substring(0, str.length) == str => DelimiterMatcher(str, Continue)
      case str                                              => DelimiterMatcher(str, FailedMatch)
    }

}
