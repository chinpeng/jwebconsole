package org.jwebconsole.server.readmodel.hostlist

import akka.actor.{ActorRef, Stash, ActorLogging, Actor}
import org.jwebconsole.server.util.{ErrorMessages, ErrorMessage, AppConstants}
import akka.util.Timeout
import org.jwebconsole.server.context.common._
import org.jwebconsole.server.context.host._
import scala.concurrent.Future
import scala.util.Success
import org.jwebconsole.server.context.common.ResponseMessage
import scala.util.Failure
import scala.Some
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor
import java.sql.SQLException

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
    case SimpleHostViewListRequest =>
      makeResponse(dao.getAll)
    case SimpleHostViewRequest(id) =>
      makeResponse(dao.getSingle(id))
    case msg =>
      log.warning("Received unhandled message to Host View" + msg)
  }

  def processFailure(e: Throwable, currentSender: ActorRef): Unit = {
    def respondWithError(msg: ErrorMessage): Unit = {
      log.error(e, msg.message)
      currentSender ! ResponseMessage(error = Some(msg))
    }
    e match {
      case e: SQLException => respondWithError(ErrorMessages.DbConnectionFailureMessage)
      case e: NoSuchElementException => respondWithError(ErrorMessages.HostNotFoundMessage)
      case _ => respondWithError(ErrorMessages.UnknownErrorMessage)
    }
  }

  def makeResponse[T](queryMethod: => T): Unit = {
    val current = sender()
    Future(queryMethod) onComplete {
      case Failure(e) =>
        processFailure(e, current)
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

case object SimpleHostViewListRequest

case class SimpleHostViewRequest(hostId: String)
