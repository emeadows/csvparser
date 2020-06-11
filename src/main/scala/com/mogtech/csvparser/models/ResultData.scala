package com.mogtech.csvparser.models

case class ResultData(errors: List[ErrorType], current: Option[String], goodData: Option[String], balanced: Boolean)
