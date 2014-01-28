package org.jwebconsole.server.context.host

import akka.actor.ActorRef

trait HostCommand {
  def id: String
}

case class CreateHostCommand(id: String = "", name: String, port: Int, user: String = "", password: String = "") extends HostCommand

case class ChangeHostCommand(id: String, name: String, port: Int, user: String = "", password: String = "") extends HostCommand

case class DeleteHostCommand(id: String) extends HostCommand

case class WithSender(source: ActorRef, cmd: HostCommand)