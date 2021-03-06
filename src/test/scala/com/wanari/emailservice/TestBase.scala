package com.wanari.emailservice

import akka.actor.ActorSystem
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

trait TestBase extends WordSpecLike with Matchers with MockitoSugar with BeforeAndAfterAll {

  val timeout = 20.second

  def await[T](f: Future[T]): T = Await.result(f, timeout)

  def withActorSystem[R](block: ActorSystem => R): R = {
    val as = ActorSystem()
    try {
      block(as)
    } finally {
      await(as.terminate())
    }
  }
}
