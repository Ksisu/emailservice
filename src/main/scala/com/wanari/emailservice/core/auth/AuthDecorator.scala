package com.wanari.emailservice.core.auth

import com.wanari.emailservice.Api
import com.wanari.emailservice.core.config.ConfigService
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.Credentials

object AuthDecorator {
  implicit class AuthApi(api: Api) {
    def withAuth(implicit config: ConfigService): Api = {
      val authenticator: AuthenticatorPF[Unit] = {
        case p: Credentials.Provided if p.verify(config.getApiSecret) =>
      }

      () =>
        authenticateBasicPF(realm = "", authenticator) { _ =>
          api.route()
        }
    }
  }
}
