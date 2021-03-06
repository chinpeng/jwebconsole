package org.jwebconsole.server.worker

import akka.actor.{Props, ActorLogging, ActorRef, Actor}
import org.jwebconsole.server.context.host.{HostCreatedEvent, HostDeletedEvent, HostParametersChangedEvent}
import org.jwebconsole.server.jmx.JMXConnectionFactory
import org.jwebconsole.server.readmodel.hostlist.{AvailableHostsList, SimpleHostView}

class HostWorkerProducerActor(private val hostCommandHandler: ActorRef,
                              private val connectionFactory: JMXConnectionFactory) extends Actor with ActorLogging {

  var workers = Map.empty[String, ActorRef]

  override def receive: Receive = {
    receiveAvailableHosts orElse receiveEvents orElse logUnknown
  }

  def receiveAvailableHosts: Receive = {
    case AvailableHostsList(hosts) => hosts.foreach {
      host =>
        val worker = createWorker(host)
        worker ! StartWork()
        workers.get(host.id).map(_ ! StopWork())
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
    workers.get(event.id).map(_ ! StopWork())
    workers -= event.id
    log.debug("Stopped worker for host with id" + event.id)
  }

  def createHost(event: HostCreatedEvent): Unit = {
    val view = SimpleHostView(event.id, event.name, event.port, event.user, event.password)
    workers.get(event.id).map(ref => ref ! StopWork())
    val worker = createWorker(view)
    workers += (event.id -> worker)
    worker ! StartWork()
  }


  def createWorker(view: SimpleHostView): ActorRef = {
    context.actorOf(Props(new HostWorkerActor(view, hostCommandHandler, connectionFactory)))
  }

  def logUnknown: Receive = {
    case _ => log.warning("Received unknown message to worker producer")
  }

}
