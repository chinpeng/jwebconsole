package org.jwebconsole.server.context.host

import akka.actor.ActorRef

trait HostCommand {
  def id: Long
}

case class CreateHostCommand(id: Long, name: String, port: Int, user: String = "", password: String = "") extends HostCommand

case class ChangeHostCommand(id: Long, name: String, port: Int, user: String, password: String) extends HostCommand

case class DeleteHostCommand(id: Long) extends HostCommand

case class WithSender(source: ActorRef, cmd: HostCommand)