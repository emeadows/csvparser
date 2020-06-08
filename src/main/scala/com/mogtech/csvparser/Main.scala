package com.mogtech.csvparser

import com.mogtech.csvparser.routing.Routes
import com.mogtech.csvparser.utils.ConfigurationSettings

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  private val conf: ConfigurationSettings = ConfigurationSettings.conf

  val routes = new Routes(conf)
  routes.start().foreach { shutdown =>
    val _ = sys.addShutdownHook {
      shutdown(Duration.Inf)
    }
  }
}
