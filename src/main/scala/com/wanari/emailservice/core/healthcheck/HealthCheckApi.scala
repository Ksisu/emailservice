package com.wanari.emailservice.core.healthcheck

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.wanari.emailservice.Api
import com.wanari.emailservice.util.TracingDirectives.trace

import scala.concurrent.Future

class HealthCheckApi(implicit service: HealthCheckService[Future]) extends Api {

  def route(): Route = {
    path("healthCheck") {
      get {
        trace("HealthCheck") { _ =>
          onSuccess(service.getStatus) { result =>
            complete(result)
          }
        }
      }
    }
  }
}
