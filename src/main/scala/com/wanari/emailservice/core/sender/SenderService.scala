package com.wanari.emailservice.core.sender

trait SenderService[F[_]] {
  def send(from: String, to: String, title: String, body: String): F[Unit]
}
