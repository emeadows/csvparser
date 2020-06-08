package com.mogtech.csvparser.utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConfigurationSettingsSpec extends AnyWordSpec with Matchers {

  "ConfigurationSettings" should {
    "load config correctly" in {
      ConfigurationSettings.conf shouldBe ConfigurationSettings(quotedType = "\"", newLine = "\n", delimiter = ",")
    }
  }

}
