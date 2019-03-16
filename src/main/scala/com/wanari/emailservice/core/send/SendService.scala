package com.wanari.emailservice.core.send

trait SendService[F[_]] {
  def send(
      email: String,
      templateId: String,
      titleArguments: Map[String, Any],
      bodyArguments: Map[String, Any]
  ): F[Unit]
}
