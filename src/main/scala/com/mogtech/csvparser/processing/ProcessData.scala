package com.mogtech.csvparser.processing

import com.mogtech.csvparser.models.SplitByQuotes
import com.mogtech.csvparser.processing.DelimiterMatcher.matchOnQuoteOrDelimiter
import com.mogtech.csvparser.processing.QuoteMatcher.matchOnQuote

import scala.annotation.tailrec

object ProcessData {

  def splitDataOnDelimiter(input: String, quoteType: String, delimiter: String): List[String] = {

    @tailrec
    def splitStrings(input: String, result: List[String], current: String, partialMatch: String, needClosingQuote: Boolean): List[String] =
      input.headOption match {
        case None if current.isEmpty && partialMatch.isEmpty                => result
        case None                                                           => result.appended(s"$current$partialMatch")
        case _ if input == delimiter || s"$partialMatch$input" == delimiter => result ++ List(current, "")
        case Some(char) if needClosingQuote =>
          val matchData = matchOnQuote(char, partialMatch, quoteType)
          val update    = matchData.processQuoteMatcher(result, current, needClosingQuote)
          splitStrings(input.tail, update.result, update.current, update.partialMatch, update.needClosingQuote)
        case Some(char) =>
          val matchData = matchOnQuoteOrDelimiter(char, partialMatch, quoteType, delimiter)
          val update    = matchData.processDelimiterMatcher(result, current, needClosingQuote)
          splitStrings(input.tail, update.result, update.current, update.partialMatch, update.needClosingQuote)
      }

    splitStrings(input = input, result = Nil, current = "", partialMatch = "", needClosingQuote = false)
  }

  def splitByQuotes(input: String, quoteType: String): SplitByQuotes = {

    @tailrec
    def splitStrings(input: String, result: SplitByQuotes, current: String, partialMatch: String): SplitByQuotes =
      input.headOption match {
        case None if current.isEmpty && partialMatch.isEmpty => result
        case None                                            => SplitByQuotes.update(s"$current$partialMatch", result)
        case Some(char) =>
          matchOnQuote(char, partialMatch, quoteType) match {
            case QuoteMatcher(update, FailedMatch) => splitStrings(input.tail, result, s"$current$update", "")
            case QuoteMatcher(update, Continue)    => splitStrings(input.tail, result, current, update)
            case QuoteMatcher(_, IsQuoteMatch)     => splitStrings(input.tail, SplitByQuotes.updateWithNewQuote(current, result), "", "")
          }

      }

    splitStrings(input = input, result = SplitByQuotes(Nil, input, balanced = true), current = "", partialMatch = "")
  }

}
