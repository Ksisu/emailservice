package com.wanari.emailservice.core.send

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.wanari.emailservice.Api
import com.wanari.emailservice.core.send.SendApi._
import org.slf4j.LoggerFactory
import spray.json.{JsArray, JsBoolean, JsNumber, JsObject, JsString, RootJsonFormat}

import scala.concurrent.Future
import scala.util.{Failure, Success}
import com.wanari.emailservice.util.TracingDirectives._

class SendApi(implicit service: SendService[Future]) extends Api {
  private val logger = LoggerFactory.getLogger(classOf[SendApi])

  def route(): Route = {
    path("send") {
      post {
        entity(as[EmailRequest]) { data =>
          trace("Send") { _ =>
            onComplete(
              service.send(
                data.email,
                data.templateId,
                convertToMap(data.titleArguments),
                convertToMap(data.bodyArguments)
              )
            ) {
              case Success(_) => complete(StatusCodes.OK)
              case Failure(e) =>
                logger.error("Email send failed", e)
                complete(StatusCodes.InternalServerError)
            }
          }
        }
      }
    }
  }
}

object SendApi {
  import spray.json.DefaultJsonProtocol._

  case class EmailRequest(email: String, templateId: String, titleArguments: JsObject, bodyArguments: JsObject)
  implicit val emailRequestDataFormat: RootJsonFormat[EmailRequest] = jsonFormat4(EmailRequest)

  def convertToMap(data: JsObject): Map[String, Any] = {
    data.fields.collect {
      case (key, JsString(value))  => key -> value
      case (key, JsBoolean(value)) => key -> value
      case (key, JsNumber(value))  => key -> value
      case (key, JsArray(list)) =>
        key -> list.collect {
          case value: JsObject => convertToMap(value)
        }
    }
  }
}
