package org.jwebconsole.server.worker

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import org.jwebconsole.server.context.host.model.{SimpleHostView, AvailableHostsList}
import org.jwebconsole.server.context.host.{HostCreatedEvent, HostDeletedEvent, HostParametersChangedEvent}

class HostWorkerProducerActor(hostCommandHandler: ActorRef) extends Actor with ActorLogging {

  var workers = Map.empty[String, ActorRef]

  override def receive: Receive = {
    receiveAvailableHosts orElse receiveEvents orElse logUnknown
  }

  def receiveAvailableHosts: Receive = {
    case AvailableHostsList(hosts) => hosts.foreach {
      host =>
        val worker = createWorker(host)
        workers += (host.id -> worker)
    }
  }

  def receiveEvents: Receive = {
    case ev: HostParametersChangedEvent =>
      changeHostParams(ev)
    case ev: HostDeletedEvent =>
      deleteHost(ev)
    case ev: HostCreatedEvent =>
      createHost(ev)
  }

  def changeHostParams(event: HostParametersChangedEvent): Unit = {
    workers.get(event.id).map(_ ! event)
  }

  def deleteHost(event: HostDeletedEvent): Unit = {
    val worker = workers(event.id)
    context.stop(worker)
    workers = workers - event.id
    log.debug("Stopped worker for host with id" + event.id)
  }

  def createHost(event: HostCreatedEvent): Unit = {
    val view = SimpleHostView(event.id, event.name, event.port)
    workers.get(event.id).map(ref => context.stop(ref))
    val worker = createWorker(view)
    workers += (event.id -> worker)
    worker ! StartWork()
  }


  def createWorker(view: SimpleHostView): ActorRef = {
    context.actorOf(Props(new HostWorker(view, hostCommandHandler)))
  }

  def logUnknown: Receive = {
    case _ => log.warning("Received unknown message to worker producer")
  }

}
