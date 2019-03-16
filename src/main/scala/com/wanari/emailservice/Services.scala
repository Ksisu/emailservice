package com.wanari.emailservice

import com.wanari.emailservice.core.config.{ServerConfig, ServerConfigImpl}
import com.wanari.emailservice.core.healthcheck.{HealthCheckService, HealthCheckServiceImpl}
import com.wanari.emailservice.core.send.{SendService, SendServiceImpl}
import com.wanari.emailservice.core.sender.{SenderService, SmtpService}
import com.wanari.emailservice.core.template.{MustacheTemplateService, TemplateService}

import scala.concurrent.{ExecutionContext, Future}

trait Services[F[_]] {
  implicit val configService: ServerConfig[F]
  implicit val healthCheckService: HealthCheckService[F]
  implicit val senderService: SenderService[F]
  implicit val templateService: TemplateService[F]
  implicit val sendService: SendService[F]
}

class RealServices(implicit ec: ExecutionContext) extends Services[Future] {
  import cats.instances.future._

  implicit lazy val configService: ServerConfig[Future] = new ServerConfigImpl[Future]

  implicit lazy val healthCheckService: HealthCheckService[Future] = new HealthCheckServiceImpl[Future]
  implicit lazy val senderService: SenderService[Future]           = new SmtpService()
  implicit lazy val templateService: TemplateService[Future]       = new MustacheTemplateService()
  implicit lazy val sendService: SendService[Future]               = new SendServiceImpl[Future]()
}
