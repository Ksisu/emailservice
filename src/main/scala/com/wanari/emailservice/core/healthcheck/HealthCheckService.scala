package com.wanari.emailservice.core.healthcheck

import com.wanari.emailservice.core.healthcheck.HealthCheckService.HealthCheckResult

trait HealthCheckService[F[_]] {
  def getStatus: F[HealthCheckResult]
}

object HealthCheckService {
  import spray.json.DefaultJsonProtocol._
  final case class HealthCheckResult(success: Boolean, version: String)
  implicit val healthCheckResultFormat = jsonFormat2(HealthCheckResult)
}
