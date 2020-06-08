package com.mogtech.csvparser.utils

import com.typesafe.config.ConfigFactory
import pureconfig.ConfigSource

case class ConfigurationSettings(quotedType: String, newLine: String, delimiter: String)

object ConfigurationSettings {

  import pureconfig.generic.auto._

  val conf: ConfigurationSettings = ConfigSource.fromConfig(ConfigFactory.load()).loadOrThrow[ConfigurationSettings]

}
