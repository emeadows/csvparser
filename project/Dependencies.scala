import sbt._

object Dependencies {

  private val akkaHttpVersion:   String = "10.1.11"
  private val akkaStreamVersion: String = "2.6.5"
  private val pureConfigVersion: String = "0.12.3"
  private val scalaTestVersion:  String = "3.1.1"

  val all: Seq[ModuleID] = ProductionDependencies.values ++ TestDependencies.values

  private[this] object ProductionDependencies {

    val values: Seq[ModuleID] =
      Seq(
        "com.typesafe.akka"     %% "akka-http"   % akkaHttpVersion,
        "com.typesafe.akka"     %% "akka-stream" % akkaStreamVersion,
        "com.github.pureconfig" %% "pureconfig"  % pureConfigVersion
      )

  }

  private[this] object TestDependencies {

    lazy val values: Seq[ModuleID] =
      Seq(
        "org.scalatest"     %% "scalatest"         % scalaTestVersion  % Test,
        "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion   % Test,
        "com.typesafe.akka" %% "akka-testkit"      % akkaStreamVersion % Test
      )

  }
}
