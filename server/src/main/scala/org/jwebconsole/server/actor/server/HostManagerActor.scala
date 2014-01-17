package org.jwebconsole.server.actor.server

import akka.persistence.EventsourcedProcessor
import org.jwebconsole.server.model._
import akka.actor.{ActorLogging, Props, ActorRef}
import org.jwebconsole.server.model.ConnectHost
import org.jwebconsole.server.model.HostConnected
import org.jwebconsole.server.model.HostInfo

class HostManagerActor extends EventsourcedProcessor with ActorLogging {

  override def preStart() = {
    super.preStart()
    context.system.eventStream.subscribe(self, classOf[GetServerStatusResp])
    context.system.eventStream.subscribe(self, classOf[ConnectHostResp])
  }

  var hostActors = Map.empty[HostInfo, ActorRef]

  def receiveReplay: Receive = {
    case ConnectHost(info) => {
      connect(info)
      if (recoveryFinished) {
        startListeners()
      }
    }
  }

  def startListeners() = hostActors.foreach(listen)

  def listen(pair: (HostInfo, ActorRef)) = pair._2 ! StartListen

  def connect(info: HostInfo): Unit = {
    val listener = context.actorOf(Props(new HostListenerActor(info)))
    hostActors += (info -> listener)
  }

  def receiveCommand: Receive = {
    case ConnectHostResp(ConnectHost(info), ref: ActorRef) =>
      persist(info) {
        host =>
          connect(host)
          hostActors(host) ! StartListen
          ref ! HostConnected(info)
      }
    case GetServerStatusResp(GetServerStatus(host: HostInfo), ref) =>
      checkServerStatus(host, ref)
  }

  def checkServerStatus(host: HostInfo, ref: ActorRef) {
    hostActors.get(host) match {
      case None => ref ! JMXHostStatus(available = false, connected = false)
      case Some(v) => Unit
    }
  }

  case object RecoveryFinished

}
