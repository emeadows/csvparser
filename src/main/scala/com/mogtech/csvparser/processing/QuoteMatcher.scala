package com.mogtech.csvparser.processing

case class QuoteMatcher(update: String, matched: QuoteMatchedType) {

  def processQuoteMatcher(result: List[String], current: String, needClosingQuote: Boolean): ProcessedMatch =
    matched match {
      case IsQuoteMatch =>
        ProcessedMatch(result, current = s"$current$update", partialMatch = "", needClosingQuote = !needClosingQuote)
      case Continue =>
        ProcessedMatch(result, current = current, partialMatch = update, needClosingQuote = needClosingQuote)
      case FailedMatch =>
        ProcessedMatch(result, current = s"$current$update", partialMatch = "", needClosingQuote = needClosingQuote)
    }
}

object QuoteMatcher {

  def matchOnQuote(char: Char, partialMatch: String, quoteType: String): QuoteMatcher =
    s"$partialMatch$char" match {
      case `quoteType`                                      => QuoteMatcher(quoteType, IsQuoteMatch)
      case str if quoteType.substring(0, str.length) == str => QuoteMatcher(str, Continue)
      case str                                              => QuoteMatcher(str, FailedMatch)
    }
}
