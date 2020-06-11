package com.mogtech.csvparser.models

case class ResultData(errors: List[ErrorType], current: Option[String], goodData: List[String], balanced: Boolean)
