package com.wanari.emailservice.core.send

import cats.Monad
import com.wanari.emailservice.core.config.ConfigService
import com.wanari.emailservice.core.sender.SenderService
import com.wanari.emailservice.core.template.TemplateService

class SendServiceImpl[F[_]: Monad](
    implicit config: ConfigService,
    templateService: TemplateService[F],
    senderService: SenderService[F]
) extends SendService[F] {

  override def send(
      email: String,
      templateId: String,
      titleArguments: Map[String, Any],
      bodyArguments: Map[String, Any]
  ): F[Unit] = {

    import cats.syntax.flatMap._
    import cats.syntax.functor._

    for {
      title      <- templateService.renderTitle(templateId, titleArguments)
      body       <- templateService.renderBody(templateId, bodyArguments)
      sendResult <- senderService.send(config.getEmailFrom, email, title, body)
    } yield sendResult
  }
}
