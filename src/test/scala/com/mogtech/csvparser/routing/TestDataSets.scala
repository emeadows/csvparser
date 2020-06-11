package com.mogtech.csvparser.routing

import java.nio.charset.StandardCharsets

import akka.http.scaladsl.model.{ HttpCharset, HttpCharsets }

trait TestDataSets {

  case class UploadData(encoding: HttpCharset, filename: String, data: String)

  val baseTestFile: UploadData = UploadData(
    HttpCharsets.`UTF-8`,
    "testfile1",
    "something,\"something with a line break\n\",\"\nsomething with \nmany line breaks\n\",more things,\"something plane in quote\" \n something2,\"something2 with a line break\n\",\"\nsomething2 with \nmany line breaks\n\",more things,\"something2 plane in quote\""
  )

  val baseTestFileWithOtherLineBreaks: UploadData = UploadData(
    HttpCharsets.`UTF-8`,
    "testfile1",
    "something,\"something with a line break**\",\"**something with **many line breaks**\",more things,\"something plane in quote\" ** something2,\"something2 with a line break**\",\"**something2 with **many line breaks**\",more things,\"something2 plane in quote\""
  )

  val testFile: UploadData = UploadData(
    HttpCharsets.`UTF-8`,
    "testfile1",
    "Ccy,To,Ccy,From,Rate,Online,,,Curremcy,Pair\nNOK,,USD,0.106888996,0.106084037,1.007587931\nILS,,USD,0.289477493,0.287959455,1.005271707\nSGD,,CHF,0.6831,0.684477726,0.997987187\nHUF,,USD,0.003289907,0.003261815,1.00861226\nKZT,,USD,0.00249,0.00242478,1.026897286,,KZT,USD\nHKD,,USD,0.12903,0.129030038,0.999999704\nKZT,,GBP,0.00198,0.00197295,1.003573329,,KZT,GBP\nXAG,,CHF,16.8897,16.6022,1.017316982,,XAG,CHF\nAUD,,GBP,0.551,0.551026051,0.999952723\nSEK,,USD,0.108858941,0.107994471,1.008004761"
  )

  val testFileWithLineBreaksAndRandomQuotes: UploadData = UploadData(HttpCharsets.`UTF-8`, "testfile2", "KZT\"K\nZ\"T\" USD")

  val testFileWithLineRandomQuotes: UploadData = UploadData(
    HttpCharsets.`UTF-8`,
    "testfile3",
    "Ccy To,Ccy From,Rate,Online,,,Curremcy Pair\nHUF,\" USD\n\n\",0.003289907,0.003261815,1.00861226,,\nKZT, USD,\"0.00\"\"249\",0.00242478,#VALUE!,,KZT USD"
  )

  val germanTestFile: UploadData = UploadData(
    HttpCharsets.`UTF-8`,
    "testfile4",
    "Auch gibt es niemanden, der den Schmerz an sich liebt, sucht oder wünscht, nur, weil er \nSchmerz ist, es sei denn, es kommt zu zufälligen Umständen, in denen Mühen und Schmerz ihm große Freude bereiten\nkönnen. Um ein triviales Beispiel zu nehmen, wer von uns unterzieht sich je \nanstrengender körperlicher Betätigung, außer um Vorteile daraus zu ziehen? Aber\n wer hat irgend ein Recht, einen Menschen zu tadeln, der die Entscheidung trifft, eine Freude zu genießen, die keine unangenehmen, Folgen hat, oder einen, der Schmerz vermeidet, welcher keine daraus resultierende Freude, nach sich zieht? Auch gibt es niemanden, der den Schmerz an sich liebt, sucht oder wünscht, nur, weil er Schmerz ist, es sei denn, es kommt zu zufälligen "
  )

  val germanTestFileInDifferentEncoding: UploadData = UploadData(
    HttpCharsets.`ISO-8859-1`,
    "testfile5",
    new String(
      "Auch gibt es niemanden, der den Schmerz an sich liebt, sucht oder wünscht, nur, weil er \nSchmerz ist, es sei denn, es kommt zu zufälligen Umständen, in denen Mühen und Schmerz ihm große Freude bereiten\nkönnen. Um ein triviales Beispiel zu nehmen, wer von uns unterzieht sich je \nanstrengender körperlicher Betätigung, außer um Vorteile daraus zu ziehen? Aber\n wer hat irgend ein Recht, einen Menschen zu tadeln, der die Entscheidung trifft, eine Freude zu genießen, die keine unangenehmen, Folgen hat, oder einen, der Schmerz vermeidet, welcher keine daraus resultierende Freude, nach sich zieht? Auch gibt es niemanden, der den Schmerz an sich liebt, sucht oder wünscht, nur, weil er Schmerz ist, es sei denn, es kommt zu zufälligen "
        .getBytes(StandardCharsets.UTF_8),
      StandardCharsets.ISO_8859_1
    )
  )
}
