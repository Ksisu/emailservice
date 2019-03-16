package com.wanari.emailservice.core.config

import cats.Monad
import com.typesafe.config.{Config, ConfigFactory}

trait ServerConfig[F[_]] {
  def getVersion: F[String]
}

class ServerConfigImpl[F[_]: Monad]() extends ServerConfig[F] {
  import cats.syntax.applicative._

  private lazy val conf: Config = ConfigFactory.load

  lazy val getVersion: F[String] = conf.getString("version").pure
}
