package com.mogtech.csvparser.processing

import com.mogtech.csvparser.models.{ ErrorType, ResultData, SplitByQuotes }
import com.mogtech.csvparser.utils.ConfigurationSettings

import scala.util.Try

case class FileProcessing(settings: ConfigurationSettings) {

  def processSplitsByLineBreak(input: String): Either[Throwable, SplitByQuotes] =
    Try(ProcessData.splitByQuotes(input, settings.quotedType)).toEither

  def processInputStringWithDelimiter(input: String): Either[Throwable, List[String]] =
    Try(ProcessData.splitDataOnDelimiter(input, settings.quotedType, settings.delimiter)).toEither

  def rejoinStringsWhereNeeded(resultData: ResultData, next: Either[Throwable, SplitByQuotes]): ResultData =
    next match {
      case Left(error: Throwable) => resultData.copy(errors = resultData.errors.appended(ErrorType(error.getLocalizedMessage)))
      case Right(SplitByQuotes(_, input, true)) if resultData.balanced =>
        processInputStringWithDelimiter(updateAsGoodData(resultData.current, input)) match {
          case Right(data) =>
            resultData.copy(current = None, goodData = data, balanced = true)
          case Left(error) =>
            resultData.copy(errors = resultData.errors.appended(ErrorType(error.getLocalizedMessage)))
        }
      case Right(SplitByQuotes(_, input, true)) =>
        resultData.copy(current = addLineBreak(resultData.current, input), goodData = Nil, balanced = false)
      case Right(SplitByQuotes(_, input, false)) if resultData.balanced =>
        resultData.copy(current = addLineBreak(resultData.current, input), goodData = Nil, balanced = false)
      case Right(SplitByQuotes(_, input, false)) =>
        processInputStringWithDelimiter(updateAsGoodData(resultData.current, input)) match {
          case Right(data) =>
            resultData.copy(current = None, goodData = data, balanced = true)
          case Left(error) =>
            resultData.copy(errors = resultData.errors.appended(ErrorType(error.getLocalizedMessage)))
        }

    }

  def addLineBreak(previous: Option[String], input: String): Option[String] = Some(s"${previous.getOrElse("")}$input${settings.newLine}")

  def updateAsGoodData(previous: Option[String], input: String): String =
    s"${previous.getOrElse("")}${settings.quotedType}$input"
}
