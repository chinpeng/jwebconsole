package org.jwebconsole.server.actor.server

import akka.actor.{ActorRef, Actor}
import org.jwebconsole.server.jmx.{JMXConnectionReceiver, JMXWrapper}
import org.jwebconsole.server.model._
import java.util.concurrent.Executor
import scala.concurrent.{Future, ExecutionContext}
import org.jwebconsole.server.model.HostInfo
import org.jwebconsole.server.model.JMXHostStatus
import org.jwebconsole.server.model.GetServerStatus

class HostListenerActor(val host: HostInfo) extends Actor {

  context.system.eventStream.subscribe(self, classOf[GetServerStatusResp])

  implicit val exec = context.dispatcher.asInstanceOf[Executor with ExecutionContext]
  lazy val jmx = JMXWrapper(host, new JMXConnectionReceiver)

  def receive: Receive = {
    case GetServerStatusResp(GetServerStatus(host), ref) =>
      sendFutureResponse(ref)
    case GetHostWithStatus() =>
      responseWithHost()
  }

  def responseWithHost() {
    val cur = sender
    val f: Future[JMXHostStatus] = Future(jmx.status)
    f.map(cur ! HostWithStatus(host, _))
  }

  def sendFutureResponse(ref: ActorRef): Unit = {
    val f: Future[JMXHostStatus] = Future(jmx.status)
    f.map(ref ! _)
  }

}

case class GetHostWithStatus()
