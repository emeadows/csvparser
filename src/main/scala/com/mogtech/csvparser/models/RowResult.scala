package com.mogtech.csvparser.models

case class RowResult(errors: List[ErrorType], success: List[String])

object RowResult {

  implicit class ListEitherOps(dataIn: List[Either[ErrorType, String]]) {

    def toRowResult: RowResult =
      dataIn.foldLeft(RowResult(Nil, Nil)) { (result, input) =>
        input match {
          case Left(error) => result.copy(errors  = result.errors.appended(error))
          case Right(str)  => result.copy(success = result.success.appended(str))
        }
      }
  }

}
