package com.mogtech.csvparser.processing

sealed trait MatchedType
case object IsQuoteMatch extends MatchedType
case object IsDelimiterMatch extends MatchedType
case object Continue extends MatchedType
case object FailedMatch extends MatchedType
