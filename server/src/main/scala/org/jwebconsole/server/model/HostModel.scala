package org.jwebconsole.server.model

import akka.actor.ActorRef


sealed trait HostModel

sealed trait ResponseMessage

trait WithResponder[T] {
  def responder: ActorRef

  def body: T
}

object WithResponder {
  def apply(request: Any, actor: ActorRef) = {
    request match {
      case msg: GetServerStatus => GetServerStatusResp(msg, actor)
      case msg: ConnectHost => ConnectHostResp(msg, actor)
    }
  }
}

case class HostInfo(name: String, port: Int) extends HostModel

case class ConnectHost(info: HostInfo) extends HostModel

case class DisconnectHost(info: HostInfo) extends HostModel

case object StartListen extends HostModel

case class GetServerStatus(info: HostInfo) extends HostModel

case class JMXHostStatus(available: Boolean, connected: Boolean) extends ResponseMessage

case class HostConnected(info: HostInfo) extends ResponseMessage

case class GetServerStatusResp(body: GetServerStatus, responder: ActorRef) extends WithResponder[GetServerStatus]

case class ConnectHostResp(body: ConnectHost, responder: ActorRef) extends WithResponder[ConnectHost]
