package com.wanari.emailservice.core.send

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpRequest, MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.wanari.emailservice.TestBase
import com.wanari.emailservice.core.send.SendApi.EmailRequest
import com.wanari.emailservice.core.testutil.{DummyConfigService, DummyTemplateService, SenderServiceMock}
import spray.json.{JsArray, JsFalse, JsNull, JsNumber, JsObject, JsString, JsTrue}

import scala.concurrent.Future

class SendApiSpec extends TestBase with ScalatestRouteTest {

  "SendApi" should {
    "convert JsObject to scalate argument map" should {
      "empty object" in {
        SendApi.convertToMap(JsObject()) shouldEqual Map.empty
      }
      "filter out JsNull, JsObject" in {
        val data = JsObject(
          "a" -> JsNull,
          "b" -> JsObject("c" -> JsTrue)
        )
        SendApi.convertToMap(data) shouldEqual Map.empty
      }
      "convert JsString" in {
        val data     = JsObject("a" -> JsString("b"))
        val expected = Map("a"      -> "b")
        SendApi.convertToMap(data) shouldEqual expected
      }
      "convert JsBoolean" in {
        val data     = JsObject("a" -> JsTrue, "b" -> JsFalse)
        val expected = Map("a"      -> true, "b"   -> false)
        SendApi.convertToMap(data) shouldEqual expected
      }
      "convert JsNumber" in {
        val data     = JsObject("a" -> JsNumber(1), "b"   -> JsNumber(1L), "c"   -> JsNumber(1.1))
        val expected = Map("a"      -> BigDecimal(1), "b" -> BigDecimal(1L), "c" -> BigDecimal(1.1))
        SendApi.convertToMap(data) shouldEqual expected
      }
      "convert JsArray - keep only JsObject" in {
        val data = JsObject(
          "a" -> JsArray(JsObject("b" -> JsTrue), JsFalse, JsNumber(1), JsString("x"), JsArray(JsObject("x" -> JsTrue)))
        )
        val expected = Map("a" -> Seq(Map("b" -> true)))
        SendApi.convertToMap(data) shouldEqual expected
      }
    }

    trait ApiTestScope {
      import cats.instances.future._
      implicit val senderService   = new SenderServiceMock[Future]
      implicit val templateService = new DummyTemplateService[Future]
      implicit val config = new DummyConfigService {
        override def getEmailFrom: String = "emailFrom@test"
      }
      implicit val sendService = new SendServiceImpl[Future]()
      val routes               = new SendApi().route()

      def createSendRequest(emailRequestData: EmailRequest): HttpRequest = {
        import SendApi.emailRequestDataFormat
        import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
        val emailRequestEntity = await(Marshal(emailRequestData).to[MessageEntity])
        Post("/send").withEntity(emailRequestEntity)
      }
    }

    "POST /send" should {
      "convert input data and send email" in new ApiTestScope {
        val data    = EmailRequest("to@test", "templateid", JsObject("a" -> JsString("b")), JsObject("c" -> JsNumber(1)))
        val request = createSendRequest(data)

        request ~> routes ~> check {
          status shouldEqual StatusCodes.OK
          senderService.calledArguments shouldEqual Seq(
            ("emailFrom@test", "to@test", "templateid title: Map(a -> b)", "templateid body: Map(c -> 1)")
          )
        }
      }
    }
  }
}
