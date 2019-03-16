package com.wanari.emailservice.core.send

import cats.Id
import com.wanari.emailservice.TestBase
import com.wanari.emailservice.core.testutil.{DummyServerConfig, DummyTemplateService, SenderServiceMock}

class SendServiceImplSpec extends TestBase {

  trait TestScope {
    implicit val senderService   = new SenderServiceMock[Id]
    implicit val templateService = new DummyTemplateService[Id]
    implicit val config = new DummyServerConfig[Id] {
      override def getEmailFrom: Id[String] = "emailFrom@test"
    }
    val service = new SendServiceImpl[Id]()
  }

  "SendServiceImpl" should {
    "send email" should {
      "from email address from config" in new TestScope {
        service.send("", "", Map.empty, Map.empty)
        senderService.calledArguments.map(_._1) shouldEqual Seq("emailFrom@test")
      }
      "to email address" in new TestScope {
        service.send("to@test", "", Map.empty, Map.empty)
        senderService.calledArguments.map(_._2) shouldEqual Seq("to@test")
      }
      "render title" in new TestScope {
        service.send("", "TemplateID", Map("arg" -> true), Map.empty)
        senderService.calledArguments.map(_._3) shouldEqual Seq("TemplateID title: Map(arg -> true)")
      }
      "render body" in new TestScope {
        service.send("", "TemplateID", Map.empty, Map("arg" -> true))
        senderService.calledArguments.map(_._4) shouldEqual Seq("TemplateID body: Map(arg -> true)")
      }
    }
  }

}
