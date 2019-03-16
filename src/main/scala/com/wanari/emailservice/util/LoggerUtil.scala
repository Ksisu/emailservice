package com.wanari.emailservice.util

import java.io.PrintStream
import java.util.logging.Level

import org.slf4j.bridge.SLF4JBridgeHandler

object LoggerUtil {
  def initBridge(): Unit = {
    org.slf4j.LoggerFactory.getLogger("DUMMY_LOGGER_FOR_CLASSLOADER")

    SLF4JBridgeHandler.removeHandlersForRootLogger()
    SLF4JBridgeHandler.install()
    java.util.logging.Logger.getLogger("").setLevel(Level.FINEST)
    System.setErr(new PrintStream((_: Int) => {}))
  }
}
