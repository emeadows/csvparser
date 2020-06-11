package com.mogtech.csvparser.processing

sealed trait MatchedType
sealed trait QuoteMatchedType
case object IsQuoteMatch extends MatchedType with QuoteMatchedType
case object IsDelimiterMatch extends MatchedType
case object Continue extends MatchedType with QuoteMatchedType
case object FailedMatch extends MatchedType with QuoteMatchedType
