package com.mogtech.csvparser.processing

import DelimiterMatcher.{ matchOnQuote, matchOnQuoteOrDelimiter }
import com.mogtech.csvparser.models.ErrorType

import scala.annotation.tailrec

object ProcessData {

  def splitDataOnDelimiter(input: String, quoteType: String, delimiter: String): List[Either[ErrorType, String]] = {

    @tailrec
    def splitStrings(input:            String,
                     result:           List[Either[ErrorType, String]],
                     current:          String,
                     partialMatch:     String,
                     needClosingQuote: Boolean): List[Either[ErrorType, String]] =
      input.headOption match {
        case None if current.isEmpty => result
        case None                    => updateResult(result, needClosingQuote, current)
        case _ if input == delimiter => updateResult(result, needClosingQuote, current)
        case Some(char) if needClosingQuote =>
          val matchData = matchOnQuote(char, partialMatch, quoteType)
          val update    = matchData.processDelimiterMatcher(result, current, needClosingQuote)
          splitStrings(input.tail, update.result, update.current, update.partialMatch, update.needClosingQuote)
        case Some(char) =>
          val matchData = matchOnQuoteOrDelimiter(char, partialMatch, quoteType, delimiter)
          val update    = matchData.processDelimiterMatcher(result, current, needClosingQuote)
          splitStrings(input.tail, update.result, update.current, update.partialMatch, update.needClosingQuote)
      }

    splitStrings(input = input, result = Nil, current = "", partialMatch = "", needClosingQuote = false)
  }

  def updateResult(result: List[Either[ErrorType, String]], needClosingQuote: Boolean, current: String): List[Either[ErrorType, String]] =
    needClosingQuote match {
      case true  => result.appended(Left(ErrorType("Unbalanced Quotes", current)))
      case false => result.appended(Right(current))
    }

}
