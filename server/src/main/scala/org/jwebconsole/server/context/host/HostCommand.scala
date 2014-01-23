package org.jwebconsole.server.context.host

trait HostCommand {
  def id: Long
}

case class CreateHostCommand(id: Long, name: String, port: Int, user: String, password: String) extends HostCommand

case class ChangeHostCommand(id: Long, name: String, port: Int, user: String, password: String) extends HostCommand

case class DeleteHostCommand(id: Long) extends HostCommand