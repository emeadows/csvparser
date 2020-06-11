package com.mogtech.csvparser.processing

import com.mogtech.csvparser.models.{ ErrorType, ResultData }
import com.mogtech.csvparser.utils.ConfigurationSettings

import scala.util.Try

case class SplitByQuotes(data: List[String], original: String, balanced: Boolean)

object SplitByQuotes {
  def update(newLine: String, existing: SplitByQuotes): SplitByQuotes = existing.copy(data = existing.data.appended(newLine))

  def updateWithNewQuote(newLine: String, existing: SplitByQuotes): SplitByQuotes =
    existing.copy(data = existing.data.appended(newLine), balanced = !existing.balanced)
}

case class FileProcessing(settings: ConfigurationSettings) {

  def processSplitsByLineBreak(input: String): Either[Throwable, SplitByQuotes] =
    Try(ProcessData.splitByQuotes(input, settings.quotedType)).toEither

  def processInputStringWithDelimiter(input: String): Either[Throwable, List[String]] =
    Try(ProcessData.splitDataOnDelimiter(input, settings.quotedType, settings.delimiter)).toEither

  def rejoinStringsWhereNeeded(resultData: ResultData, next: Either[Throwable, SplitByQuotes]): ResultData =
    next match {
      case Left(error: Throwable) => resultData.copy(errors = resultData.errors.appended(ErrorType(error.getLocalizedMessage)))
      case Right(SplitByQuotes(_, input, true)) if resultData.balanced =>
        resultData.copy(current = None, goodData = updateAsGoodData(resultData.current, input), balanced = true)
      case Right(SplitByQuotes(_, input, true)) =>
        resultData.copy(current = addLineBreak(resultData.current, input), goodData = None, balanced = false)
      case Right(SplitByQuotes(_, input, false)) if resultData.balanced =>
        resultData.copy(current = addLineBreak(resultData.current, input), goodData = None, balanced = false)
      case Right(SplitByQuotes(_, input, false)) =>
        resultData.copy(current = None, goodData = updateAsGoodData(resultData.current, input), balanced = true)
    }

  def addLineBreak(previous:     Option[String], input: String): Option[String] = Some(s"${previous.getOrElse("")}$input${settings.newLine}")
  def updateAsGoodData(previous: Option[String], input: String): Option[String] = Some(s"${previous.getOrElse("")}$input")
}
