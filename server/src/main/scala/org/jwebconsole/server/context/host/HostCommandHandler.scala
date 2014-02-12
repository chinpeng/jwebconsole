package org.jwebconsole.server.context.host

import akka.actor.{ActorLogging, Props, ActorRef, Actor}
import java.util.UUID

class HostCommandHandler(validator: ActorRef) extends Actor with ActorLogging {

  val executor = context.system.dispatcher
  var hosts = Map.empty[String, ActorRef]

  def receive: Receive = {
    receiveWorkerCommand orElse receiveUserCommand
  }

  def receiveWorkerCommand: Receive = {
    case cmd: ChangeHostDataCommand =>
      log.debug("Received command from host worker: " + cmd)
      getHost(cmd) ! cmd
  }

  def receiveUserCommand: Receive = {
    case cmd: CreateHostCommand =>
      val id = UUID.randomUUID().toString
      log.debug("Received create command from user: " + cmd)
      log.debug("Newly generated ID is:  " + id)
      hosts += (id -> context.actorOf(Props(new HostARActor(id, validator))))
      hosts(id) ! WithSender(sender(), cmd.copy(id = id))
    case cmd: HostCommand =>
      log.debug("Received command from user: " + cmd)
      getHost(cmd) ! WithSender(sender(), cmd)
  }

  def getHost(cmd: HostCommand): ActorRef = hosts.get(cmd.id) match {
    case None =>
      val actor: ActorRef = context.actorOf(Props(new HostARActor(cmd.id, validator)))
      val id = cmd.id
      hosts += (id -> actor)
      actor
    case Some(v) => v
  }

}
