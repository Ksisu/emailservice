package com.wanari.emailservice.core.config

import cats.Monad
import com.typesafe.config.{Config, ConfigFactory}
import com.wanari.emailservice.core.config.ServerConfig.SmtpConfig

trait ServerConfig[F[_]] {
  def getVersion: F[String]
  def getEmailFrom: F[String]
  def getSmtp: F[SmtpConfig]
  def getTemplateDir: F[String]
}

object ServerConfig {
  case class SmtpConfig(host: String, port: Int, ssl: Boolean, username: String, password: String)
}

class ServerConfigImpl[F[_]: Monad]() extends ServerConfig[F] {
  import cats.syntax.applicative._

  private lazy val conf: Config = ConfigFactory.load

  lazy val getVersion: F[String]   = conf.getString("version").pure
  lazy val getEmailFrom: F[String] = conf.getString("email.from").pure
  lazy val getSmtp: F[SmtpConfig] = {
    SmtpConfig(
      conf.getString("smtp.host"),
      conf.getInt("smtp.port"),
      conf.getBoolean("smtp.ssl"),
      conf.getString("smtp.username"),
      conf.getString("smtp.password")
    ).pure
  }
  lazy val getTemplateDir: F[String] = conf.getString("template.dir").pure
}
