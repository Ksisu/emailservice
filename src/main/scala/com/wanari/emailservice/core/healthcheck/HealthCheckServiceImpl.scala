package com.wanari.emailservice.core.healthcheck

import cats.MonadError
import com.wanari.emailservice.core.config.ServerConfig
import com.wanari.emailservice.core.healthcheck.HealthCheckService.HealthCheckResult

class HealthCheckServiceImpl[F[_]](
    implicit F: MonadError[F, Throwable],
    config: ServerConfig[F]
) extends HealthCheckService[F] {

  import cats.syntax.applicativeError._
  import cats.syntax.functor._

  def getStatus: F[HealthCheckResult] = {
    for {
      version <- config.getVersion.recover { case _ => "" }
    } yield {
      val success = !version.isEmpty
      HealthCheckResult(success, version)
    }
  }
}
