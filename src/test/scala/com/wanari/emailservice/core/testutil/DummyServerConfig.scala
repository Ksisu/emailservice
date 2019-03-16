package com.wanari.emailservice.core.testutil

import cats.Applicative
import com.wanari.emailservice.core.config.ServerConfig

class DummyServerConfig[F[_]: Applicative] extends ServerConfig[F] {
  override def getVersion: F[String]               = ???
  override def getEmailFrom: F[String]             = ???
  override def getSmtp: F[ServerConfig.SmtpConfig] = ???
  override def getTemplateDir: F[String]           = ???
}
