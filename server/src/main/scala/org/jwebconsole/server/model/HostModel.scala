package org.jwebconsole.server.model

import akka.actor.ActorRef


sealed trait HostModel
sealed trait ResponseMessage

case class HostInfo(name: String, port: Int) extends HostModel

case class ConnectHost(info: HostInfo) extends HostModel

case class DisconnectHost(info: HostInfo) extends HostModel

case object StartListen extends HostModel

case class GetServerStatus(info: HostInfo) extends HostModel

case class JMXHostStatus(available: Boolean, connected: Boolean) extends ResponseMessage

case class HostConnected(info: HostInfo) extends ResponseMessage

case class MsgWithResponder[T](body: T, resp: ActorRef)