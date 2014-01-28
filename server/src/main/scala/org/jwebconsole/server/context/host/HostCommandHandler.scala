package org.jwebconsole.server.context.host

import akka.actor.{Props, ActorRef, Actor}
import java.util.UUID

class HostCommandHandler() extends Actor {

  val executor = context.system.dispatcher
  var hosts = Map.empty[String, ActorRef]

  def receive: Receive = {
    case cmd: CreateHostCommand =>
      val id = UUID.randomUUID().toString
      hosts += (id -> context.actorOf(Props(new HostARActor(id))))
      hosts(id) ! WithSender(sender, cmd.copy(id = id))
    case cmd: HostCommand => {
      getHost(cmd) ! WithSender(sender, cmd)
    }
  }

  def getHost(cmd: HostCommand): ActorRef = hosts.get(cmd.id) match {
    case None =>
      val actor: ActorRef = context.actorOf(Props(new HostARActor(cmd.id)))
      val id = cmd.id
      hosts += (id -> actor)
      actor
    case Some(v) => v
  }

}
