package org.jwebconsole.server.readmodel.hostlist

import akka.actor.{Stash, ActorLogging, Actor}
import org.jwebconsole.server.util.AppConstants
import akka.util.Timeout
import org.jwebconsole.server.context.common._
import org.jwebconsole.server.context.host._
import scala.concurrent.Future
import scala.util.Success
import org.jwebconsole.server.context.common.ResponseMessage
import scala.util.Failure
import scala.Some
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor

class HostListViewActor(val dao: SimpleHostDAO) extends Actor with ActorLogging with Stash with ReadModelReplayingActor {

  implicit val timeout = Timeout(AppConstants.DefaultTimeout)
  implicit val exec = context.system.dispatcher

  def filterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostChangedEvent => true
    case _ => false
  }

  def afterRecover(): Unit = {
    log.debug("Firing connections list event")
    Future(dao.getAll).map(items => context.system.eventStream.publish(AvailableHostsList(items)))
  }

  def persistAsync(event: HostChangedEvent): Unit = {
    Future(persistReplay(event)).onComplete {
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

  def persistReplay(ev: AppEvent): Unit = {
    ev match {
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

}

case object SimpleHostViewRequest
