package com.mogtech.csvparser.processing

import com.mogtech.csvparser.models.{ ErrorType, FullResult, RowResult }
import com.mogtech.csvparser.utils.ConfigurationSettings

import scala.util.{ Failure, Success, Try }

case class FileProcessing(settings: ConfigurationSettings) {

  def processInputStringWithDelimiter(input: String, delimiter: String): RowResult = {
    import RowResult._
    Try(ProcessData.splitDataOnDelimiter(input, settings.quotedType, delimiter)) match {
      case Success(rows)      => rows.toRowResult
      case Failure(exception) => RowResult(List(ErrorType(exception.getMessage, input)), Nil)
    }
  }

  def processInputString(input: String): FullResult = {
    import FullResult._
    processInputStringWithDelimiter(input, settings.newLine) match {
      case RowResult(errors, success) =>
        success.map(row => processInputStringWithDelimiter(row, settings.delimiter)).toFullResult(errors)
    }
  }

}
