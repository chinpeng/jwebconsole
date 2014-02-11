package org.jwebconsole.server.worker

import org.jwebconsole.server.context.host.model.SimpleHostView
import akka.actor.{ActorLogging, Actor, ActorRef}
import org.jwebconsole.server.context.host.HostParametersChangedEvent

class HostWorker(host: SimpleHostView, commandHandler: ActorRef) extends Actor with ActorLogging {

  def changeHost(): Unit = {

  }

  override def receive: Receive = {
    case ev: HostParametersChangedEvent =>
      changeHost()
    case _ => log.warning("Unknown message")
  }

}

case class StartWork()
case class StopWork()
