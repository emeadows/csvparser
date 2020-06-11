package com.mogtech.csvparser.models

case class SplitByQuotes(data: List[String], original: String, balanced: Boolean)

object SplitByQuotes {
  def update(newLine: String, existing: SplitByQuotes): SplitByQuotes = existing.copy(data = existing.data.appended(newLine))

  def updateWithNewQuote(newLine: String, existing: SplitByQuotes): SplitByQuotes =
    existing.copy(data = existing.data.appended(newLine), balanced = !existing.balanced)
}
