package com.wanari.emailservice.core.config

import com.typesafe.config.{Config, ConfigFactory}
import com.wanari.emailservice.core.config.ConfigService.SmtpConfig

trait ConfigService {
  def getVersion: String
  def getEmailFrom: String
  def getSmtp: SmtpConfig
  def getTemplateDir: String
  def getApiSecret: String
}

object ConfigService {
  case class SmtpConfig(host: String, port: Int, ssl: Boolean, username: String, password: String)
}

class ConfigServiceImpl() extends ConfigService {
  private lazy val conf: Config = ConfigFactory.load

  lazy val getVersion: String   = conf.getString("version")
  lazy val getEmailFrom: String = conf.getString("email.from")
  lazy val getSmtp: SmtpConfig = {
    SmtpConfig(
      conf.getString("smtp.host"),
      conf.getInt("smtp.port"),
      conf.getBoolean("smtp.ssl"),
      conf.getString("smtp.username"),
      conf.getString("smtp.password")
    )
  }
  lazy val getTemplateDir: String = conf.getString("template.dir")
  lazy val getApiSecret: String   = conf.getString("secret")
}
