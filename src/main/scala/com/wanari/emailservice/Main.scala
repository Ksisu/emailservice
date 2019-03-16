package com.wanari.emailservice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.wanari.emailservice.util.LoggerUtil
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

object Main extends App {
  LoggerUtil.initBridge()

  lazy val logger = LoggerFactory.getLogger(classOf[App])

  implicit lazy val system           = ActorSystem("emailservice-system")
  implicit lazy val materializer     = ActorMaterializer()
  implicit lazy val executionContext = system.dispatcher

  val services = new RealServices()

  val route = Api.createApi(services)

  Http().bindAndHandle(route, "0.0.0.0", 9000).onComplete {
    case Success(_)  => logger.info("EmailService started")
    case Failure(ex) => logger.error("EmailService starting failed", ex)
  }
}
