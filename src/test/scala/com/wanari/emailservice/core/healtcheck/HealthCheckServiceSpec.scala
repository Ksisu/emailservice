package com.wanari.emailservice.core.healtcheck

import com.wanari.emailservice.TestBase
import com.wanari.emailservice.core.config.ServerConfig
import com.wanari.emailservice.core.healthcheck.HealthCheckService.HealthCheckResult
import com.wanari.emailservice.core.healthcheck.HealthCheckServiceImpl
import org.mockito.Mockito.when

import scala.util.{Failure, Success, Try}

class HealthCheckServiceSpec extends TestBase {

  trait TestScope {
    import cats.instances.try_._
    implicit val configService: ServerConfig[Try] = mock[ServerConfig[Try]]
    val service                                   = new HealthCheckServiceImpl[Try]()
  }

  "#getStatus" when {
    "ok" in new TestScope {
      when(configService.getVersion).thenReturn(Success("TestVersionMock"))
      service.getStatus.get shouldEqual HealthCheckResult(true, "TestVersionMock")
    }
    "version failed" in new TestScope {
      when(configService.getVersion).thenReturn(Failure(new Exception))
      service.getStatus.get shouldEqual HealthCheckResult(false, "")
    }
  }
}
