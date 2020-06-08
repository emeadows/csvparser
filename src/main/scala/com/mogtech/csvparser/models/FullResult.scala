package com.mogtech.csvparser.models

case class FullResult(errors: List[ErrorType], goodData: List[List[String]]) {
  def successfullyProcessed: String = goodData.map(_.mkString(",")).mkString("\n")

  def errorsThrown: String =
    errors match {
      case Nil => "All Data Processed Successfully"
      case _ =>
        s"""The following data could not be processed:
           |
           |${errors.map(et => s"${et.msg}\n\t${et.failedData}").mkString("\n")}
           |""".stripMargin
    }
}

object FullResult {
  implicit class ListEitherOps(dataIn: List[RowResult]) {

    def toFullResult(errors: List[ErrorType]): FullResult =
      dataIn.foldLeft(FullResult(errors, Nil)) { (result, data) =>
        data match {
          case RowResult(errors, success) => result.copy(errors = result.errors ++ errors, goodData = result.goodData.appended(success))
        }
      }
  }
}
