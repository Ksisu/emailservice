package com.wanari.emailservice.core.testutil

import cats.Applicative
import com.wanari.emailservice.core.sender.SenderService

class SenderServiceMock[F[_]: Applicative] extends SenderService[F] {
  import cats.syntax.applicative._

  var calledArguments = Seq.empty[(String, String, String, String)]
  override def send(from: String, to: String, title: String, body: String): F[Unit] = {
    calledArguments :+= ((from, to, title, body))
    ().pure
  }
}
