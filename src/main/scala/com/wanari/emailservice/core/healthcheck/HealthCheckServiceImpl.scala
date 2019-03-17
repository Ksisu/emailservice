package com.wanari.emailservice.core.healthcheck

import cats.Applicative
import com.wanari.emailservice.core.config.ConfigService
import com.wanari.emailservice.core.healthcheck.HealthCheckService.HealthCheckResult

import scala.util.{Success, Try}

class HealthCheckServiceImpl[F[_]: Applicative](
    implicit config: ConfigService
) extends HealthCheckService[F] {

  import cats.syntax.applicative._

  def getStatus: F[HealthCheckResult] = {
    Try(config.getVersion) match {
      case Success(version) if version.nonEmpty => HealthCheckResult(true, version).pure
      case _                                    => HealthCheckResult(false, "").pure
    }
  }
}
