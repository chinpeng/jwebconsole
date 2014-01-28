package org.jwebconsole.server.context.host.model

import akka.actor.{Stash, Props, ActorLogging, Actor}
import org.jwebconsole.server.util.AppConstants
import akka.util.Timeout
import org.jwebconsole.server.context.util.{ResponseMessage, ReplayFinished, AppEvent, GlobalEventStore}
import org.jwebconsole.server.context.host.{HostDeletedEvent, HostParametersChangedEvent, HostCreatedEvent, HostChangedEvent}
import scala.concurrent.Future
import scala.util.{Success, Failure}

class HostReadModel(dao: SimpleHostDAO) extends Actor with ActorLogging with Stash {

  implicit val timeout = Timeout(AppConstants.DefaultTimeout)
  implicit val exec = context.system.dispatcher

  override def preStart() = {
    if (!dao.exists) {
      context.become(replayingState)
      dao.createTable()
      makeReplay()
    }
  }

  def persistAsync(event: HostChangedEvent): Unit = {
    Future(updateDB(event)).onComplete {
      case Failure(e) => log.warning("Unable to persist event" + event, e)
      case Success(v) => log.info("Successfully persisted event to read DB")
    }
  }

  def receive: Receive = {
    case ev: HostChangedEvent =>
      persistAsync(ev)
    case SimpleHostViewRequest =>
      makeResponse()
  }

  def makeResponse(): Unit = {
    val current = sender
    Future(dao.getAll) onComplete {
      case Failure(e) => current ! ResponseMessage(error = "Unable to connect to Database")
      case Success(v) => current ! ResponseMessage(payload = v)
    }
  }

  def updateDB(event: HostChangedEvent): Unit = event match {
    case ev: HostCreatedEvent =>
      dao.create(SimpleHostView(ev.id, ev.name, ev.port))
    case ev: HostParametersChangedEvent =>
      dao.update(SimpleHostView(ev.id, ev.name, ev.port))
    case ev: HostDeletedEvent =>
      dao.delete(ev.id)
  }

  val replayingState: Receive = {
    case ev: HostChangedEvent =>
      updateDB(ev)
    case ReplayFinished =>
      context.become(receive)
      unstashAll()
    case _ => stash()

  }

  def makeReplay(): Unit = {
    context.actorOf(Props(new GlobalEventStore(filterFunc, Some(self))))
  }

  def filterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostChangedEvent => true
    case _ => false
  }

}

case object SimpleHostViewRequest

case class SimpleHostView(id: String, host: String, port: Int)
