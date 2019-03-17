package com.wanari.emailservice.core.config

import com.wanari.emailservice.TestBase
import com.wanari.emailservice.core.config.ConfigService.SmtpConfig

class ConfigServiceSpec extends TestBase {
  val service = new ConfigServiceImpl()
  "ConfigService" should {
    "#getVersion" in {
      service.getVersion shouldEqual "TestVersion"
    }
    "#getEmailFrom" in {
      service.getEmailFrom shouldEqual "tutelar@localhost"
    }
    "#getSmtp" in {
      service.getSmtp shouldEqual SmtpConfig(
        "localhost",
        1025,
        false,
        "user",
        "pass"
      )
    }
    "#getTemplateDir" in {
      service.getTemplateDir shouldEqual "./templates"
    }
  }
}
