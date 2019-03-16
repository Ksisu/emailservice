package com.wanari.emailservice

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.wanari.emailservice.core.healthcheck.HealthCheckApi
import com.wanari.emailservice.core.send.SendApi

import scala.concurrent.Future

trait Api {
  def route(): Route
}

object Api {
  val emptyRoute = Route(_.reject())

  def createRoute(api: Seq[Api]): Route = {
    api
      .map(_.route())
      .fold(Api.emptyRoute)(_ ~ _)
  }

  def createApi(services: Services[Future]): Route = {
    import services._

    val api = Seq(
      new HealthCheckApi(),
      new SendApi()
    )

    createRoute(api)
  }
}
