package com.wanari.emailservice.core.auth

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{BasicHttpCredentials, HttpChallenges}
import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.server.{AuthenticationFailedRejection, Route}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.wanari.emailservice.core.testutil.DummyConfigService
import com.wanari.emailservice.{Api, TestBase}

class AuthDecoratorSpec extends TestBase with ScalatestRouteTest {

  val testApi = new Api {
    override def route(): Route = {
      complete(StatusCodes.OK)
    }
  }

  implicit val config = new DummyConfigService {
    override def getApiSecret: String = "SuperSecret"
  }

  import AuthDecorator._

  val testApiWithAuth = testApi.withAuth

  "AuthDecorator" should {
    "testApi without auth - ok" in {
      Get() ~> testApi.route() ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    "testApiWithAuth without auth - reject" in {
      Get() ~> testApiWithAuth.route() ~> check {
        rejection shouldEqual AuthenticationFailedRejection(CredentialsMissing, HttpChallenges.basic(""))
      }
    }
    "testApiWithAuth with auth - ok" in {
      val credentials = BasicHttpCredentials("", "SuperSecret")
      Get() ~> addCredentials(credentials) ~> testApiWithAuth.route() ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
    "testApiWithAuth with wrong credentials - ok" in {
      val credentials = BasicHttpCredentials("", "asd")
      Get() ~> addCredentials(credentials) ~> testApiWithAuth.route() ~> check {
        rejection shouldEqual AuthenticationFailedRejection(CredentialsRejected, HttpChallenges.basic(""))
      }
    }
  }
}
