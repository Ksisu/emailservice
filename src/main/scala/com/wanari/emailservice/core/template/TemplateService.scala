package com.wanari.emailservice.core.template

trait TemplateService[F[_]] {
  def renderTitle(templateId: String, arguments: Map[String, Any]): F[String]
  def renderBody(templateId: String, arguments: Map[String, Any]): F[String]
}
