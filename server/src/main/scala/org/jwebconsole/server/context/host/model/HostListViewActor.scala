package org.jwebconsole.server.context.host.model

import akka.actor.{Stash, Props, ActorLogging, Actor}
import org.jwebconsole.server.util.AppConstants
import akka.util.Timeout
import org.jwebconsole.server.context.common._
import org.jwebconsole.server.context.host._
import scala.concurrent.Future
import org.jwebconsole.server.context.host.HostCreatedEvent
import scala.util.Success
import org.jwebconsole.server.context.host.HostParametersChangedEvent
import org.jwebconsole.server.context.common.ResponseMessage
import scala.util.Failure
import scala.Some
import org.jwebconsole.server.context.host.HostDeletedEvent

class HostListViewActor(dao: SimpleHostDAO) extends Actor with ActorLogging with Stash {

  implicit val timeout = Timeout(AppConstants.DefaultTimeout)
  implicit val exec = context.system.dispatcher

  override def preStart() = {
    if (!dao.exists) {
      context.become(replayingState)
      dao.createTable()
      makeReplay()
    } else {
      fireAvailableHosts()
    }
  }

  def fireAvailableHosts(): Unit = {
    log.debug("Firing connections list event")
    Future(dao.getAll).map(items => context.system.eventStream.publish(AvailableHostsList(items)))
  }

  def persistAsync(event: HostChangedEvent): Unit = {
    Future(updateDB(event)).onComplete {
      case Failure(e) => log.error(e, "Unable to persist event")
      case Success(v) => log.debug("Successfully persisted event to read DB")
    }
  }

  def receive: Receive = {
    case ev: HostChangedEvent =>
      persistAsync(ev)
    case SimpleHostViewRequest =>
      makeResponse()
  }

  def makeResponse(): Unit = {
    val current = sender()
    Future(dao.getAll) onComplete {
      case Failure(e) =>
        log.error(e, "Unable to connect to Database")
        current ! ResponseMessage(error = Some("Unable to connect to Database"))
      case Success(v) => current ! ResponseMessage(body = Some(v))
    }
  }

  def updateDB(event: HostChangedEvent): Unit = {
    log.debug("updating db record for event: " + event)
    event match {
      case ev: HostCreatedEvent =>
        dao.create(SimpleHostView(ev.id, ev.name, ev.port, connected = true))
      case ev: HostParametersChangedEvent =>
        dao.updateParameters(SimpleHostView(ev.id, ev.name, ev.port))
      case ev: HostDeletedEvent =>
        dao.delete(ev.id)
      case ev: HostDataChangedEvent =>
        dao.updateStatus(ev.id, ev.data.connected)
    }
  }

  val replayingState: Receive = {
    case ev: HostChangedEvent =>
      updateDB(ev)
    case ReplayFinished =>
      context.become(receive)
      fireAvailableHosts()
      unstashAll()
      log.debug("Finished replaying state, back to normal")
    case _ =>
      log.debug("Stashing event")
      stash()

  }

  def makeReplay(): Unit = {
    context.actorOf(Props(new EventStoreReplayingActor(filterFunc, self)))
  }

  def filterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostChangedEvent => true
    case _ => false
  }

}

case object SimpleHostViewRequest
