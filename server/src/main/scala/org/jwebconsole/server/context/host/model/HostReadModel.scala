package org.jwebconsole.server.context.host.model

import akka.actor.{ActorLogging, Props, Actor}
import org.jwebconsole.server.context.host.{HostDeletedEvent, HostCreatedEvent, HostParametersChangedEvent, HostChangedEvent}
import org.jwebconsole.server.context.util.{GrabbedEvents, GrabEvents, AppEvent, AppEventHandler}
import akka.pattern.ask
import org.jwebconsole.server.util.AppConstants
import scala.concurrent.{Await, Future}
import akka.util.Timeout
import scala.util.{Try, Failure, Success}

class HostReadModel(dao: SimpleHostDAO) extends Actor with ActorLogging {

  implicit val timeout = Timeout(AppConstants.DefaultTimeout)
  implicit val exec = context.system.dispatcher

  override def preStart() = {
    Try {
      if (!dao.exists) {
        val events = replayEvents()
        events.foreach(updateDB)
      }
    } match {
      case Success(v) => log.info("Read model recovery finished")
      case Failure(e) => log.error("Read model recovery failed", e)
    }
  }

  def replayEvents(): List[AppEvent] = {
    val handler = context.actorOf(Props(new AppEventHandler(eventFilterFunc)))
    val eventsFuture = (handler ? GrabEvents).asInstanceOf[Future[GrabbedEvents]]
    Await.result(eventsFuture, timeout.duration).events
  }

  def eventFilterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostChangedEvent => true
    case _ => false
  }

  def updateDB(event: AppEvent): Unit = {
    event match {
      case updated: HostParametersChangedEvent =>
        dao.update(SimpleHostView(updated.id, updated.name, updated.port))
      case created: HostCreatedEvent =>
        dao.create(SimpleHostView(created.id, created.name, created.port))
      case deleted: HostDeletedEvent =>
        dao.delete(deleted.id)
    }
  }

  def receive: Receive = {
    case ev: HostChangedEvent =>
      Future(updateDB(ev)) onComplete {
        case Success(v) =>
          log.info("successful event persist")
        case Failure(e) =>
          log.error("Failed to persist host changed event", e)
      }
  }
}

case class SimpleHostView(id: Long, host: String, port: Int)
