package org.jwebconsole.server.actor.server

import akka.persistence.EventsourcedProcessor
import org.jwebconsole.server.model._
import akka.actor.{ActorLogging, Props, ActorRef}
import org.jwebconsole.server.model.ConnectHost
import org.jwebconsole.server.model.HostInfo
import org.jwebconsole.server.actor.provider.ActorProvider
import scala.concurrent.duration._
import org.jwebconsole.server.actor.model.CheckRecoveryStatus
import akka.pattern.ask
import scala.concurrent.Future
import akka.util.Timeout

class HostManagerActor(provider: ActorProvider) extends EventsourcedProcessor with ActorLogging {

  context.system.eventStream.subscribe(self, classOf[GetServerStatusResp])
  context.system.eventStream.subscribe(self, classOf[ConnectHostResp])
  context.system.eventStream.subscribe(self, classOf[GetConnectedHostsResp])

  implicit val timeout = Timeout(5 seconds)
  implicit val execution = context.system.dispatcher

  var hostActors = Map.empty[HostInfo, ActorRef]

  lazy val timer = context.system.scheduler.schedule(1 seconds, 1 second)(self ! CheckRecoveryStatus)

  def receiveReplay: Receive = {
    case msg: HostInfo => {
      connect(msg)
    }
  }

  def startListeners() = hostActors.foreach(listen)

  def listen(pair: (HostInfo, ActorRef)) = pair._2 ! StartListen

  def connect(info: HostInfo): Unit = {
    val listener = context.actorOf(Props(new HostListenerActor(info)))
    hostActors += (info -> listener)
  }

  def checkRecoveryStatus(): Unit = {
    if (recoveryFinished && !timer.isCancelled) {
      timer.cancel()
      startListeners()
    }
  }

  def receiveCommand: Receive = {
    case ConnectHostResp(ConnectHost(info), ref: ActorRef) =>
      persist(info) {
        host =>
          connect(host)
          hostActors(host) ! StartListen
      }
    case GetServerStatusResp(GetServerStatus(host: HostInfo), ref) =>
      checkServerStatus(host, ref)
    case CheckRecoveryStatus =>
      checkRecoveryStatus()
    case GetConnectedHostsResp(_, ref) =>
      getConnectedHosts(ref)
  }

  def getConnectedHosts(sender: ActorRef): Unit = {
    val futures:List[Future[HostWithStatus]] = hostActors.map(item => (item._2 ? GetHostWithStatus()).asInstanceOf[Future[HostWithStatus]]).toList
    val result: Future[List[HostWithStatus]] = Future.sequence(futures.toList)
    result.map(sender ! ConnectedHostsResponse(_))
  }

  def checkServerStatus(host: HostInfo, ref: ActorRef) {
    hostActors.get(host) match {
      case None => ref ! JMXHostStatus(available = false, connected = false)
      case Some(v) => Unit
    }
  }

}

object HostManagerActor {

  def apply(): Props = {
    Props(new HostManagerActor(new ActorProvider))
  }

}
