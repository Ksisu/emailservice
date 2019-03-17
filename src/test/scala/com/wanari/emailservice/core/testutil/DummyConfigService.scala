package com.wanari.emailservice.core.testutil

import com.wanari.emailservice.core.config.ConfigService

class DummyConfigService extends ConfigService {
  override def getVersion: String                = ???
  override def getEmailFrom: String              = ???
  override def getSmtp: ConfigService.SmtpConfig = ???
  override def getTemplateDir: String            = ???
  override def getApiSecret: String              = ???
}
