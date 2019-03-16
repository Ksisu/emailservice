package com.wanari.emailservice.core.testutil

import cats.Applicative
import com.wanari.emailservice.core.template.TemplateService

class DummyTemplateService[F[_]: Applicative] extends TemplateService[F] {
  import cats.syntax.applicative._
  override def renderTitle(templateId: String, arguments: Map[String, Any]): F[String] =
    s"$templateId title: $arguments".pure
  override def renderBody(templateId: String, arguments: Map[String, Any]): F[String] =
    s"$templateId body: $arguments".pure
}
