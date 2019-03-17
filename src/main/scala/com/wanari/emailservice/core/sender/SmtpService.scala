package com.wanari.emailservice.core.sender

import com.wanari.emailservice.core.config.ConfigService
import org.apache.commons.mail.{DefaultAuthenticator, HtmlEmail}

import scala.concurrent.{ExecutionContext, Future}

class SmtpService(implicit ec: ExecutionContext, config: ConfigService) extends SenderService[Future] {

  override def send(from: String, to: String, title: String, body: String): Future[Unit] = {
    Future {
      val email = createNewEmail()
      email.setFrom(from)
      email.addTo(to)
      email.setSubject(title)
      email.setHtmlMsg(body)
      email.send()
    }
  }

  private def createNewEmail(): HtmlEmail = {
    val smtpConfig = config.getSmtp
    val email      = new HtmlEmail
    email.setHostName(smtpConfig.host)
    if (!smtpConfig.username.isEmpty) {
      email.setAuthenticator(new DefaultAuthenticator(smtpConfig.username, smtpConfig.password))
    }
    email.setSSLOnConnect(smtpConfig.ssl)
    if (smtpConfig.ssl) {
      email.setSslSmtpPort(smtpConfig.port.toString)
    } else {
      email.setSmtpPort(smtpConfig.port)
    }
    email
  }
}
