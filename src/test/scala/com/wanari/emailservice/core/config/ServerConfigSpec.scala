package com.wanari.emailservice.core.config

import cats.Id
import com.wanari.emailservice.TestBase

class ServerConfigSpec extends TestBase {
  "#getVersion" in {
    val service = new ServerConfigImpl[Id]()
    service.getVersion shouldEqual "TestVersion"
  }
}
