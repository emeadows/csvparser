name := "csv_parser"
organization := "com.mogtech"

val theScalacOption = Seq(
  "-unchecked",
  "-feature",
  "-deprecation",
  "-encoding",
  "utf8",
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates" // Warn if a private member is unused.
)

lazy val root = project
  .in(file("."))
  .settings(
    scalaVersion := "2.13.2",
    scalacOptions ++= theScalacOption,
    libraryDependencies ++= Dependencies.all,
    concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
    organization := "com.leonteq.eportal",
    parallelExecution in Test := false,
    fork in run := true,
    Global / onChangedBuildSource := ReloadOnSourceChanges
  )

