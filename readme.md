# CSV Parser

This is a parser that can be configurable with the following fields:

    quotedType
    newLine
    delimiter

The HTTP layer has been created using Akka Http.

## Start

    download code, run `sbt clean compile test`
    to run the app run `sbt run`

### Endpoint

    There is currenly only one endpoint, this is on localhost:80

    Enpoint is `POST` and takes FormData.
    Ensure the `Content-Type` is set with `MediaTypes` of `text/plain` and `Charset` of the relevant data type

### Improvements

    Currently the solution relys on the input being processed as a string, this could be parsed as a stream.
    All errors are related to data being parsed, further valudation could be run.
    Enpoints and urls should be externalised in config.
