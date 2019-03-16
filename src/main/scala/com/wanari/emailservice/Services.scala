package com.wanari.emailservice

import com.wanari.emailservice.core.config.{ServerConfig, ServerConfigImpl}
import com.wanari.emailservice.core.healthcheck.{HealthCheckService, HealthCheckServiceImpl}

import scala.concurrent.{ExecutionContext, Future}

trait Services[F[_]] {
  implicit val configService: ServerConfig[F]
  implicit val healthCheckService: HealthCheckService[F]
}

class RealServices(implicit ec: ExecutionContext) extends Services[Future] {
  import cats.instances.future._

  implicit lazy val configService: ServerConfig[Future] = new ServerConfigImpl[Future]

  implicit lazy val healthCheckService: HealthCheckService[Future] = new HealthCheckServiceImpl[Future]
}
