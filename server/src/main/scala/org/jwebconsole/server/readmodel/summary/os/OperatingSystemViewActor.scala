package org.jwebconsole.server.readmodel.summary.os

import akka.actor.{Stash, ActorLogging, Actor}
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor
import org.jwebconsole.server.context.common.{ResponseMessage, AppEvent}
import org.jwebconsole.server.context.host.{HostDataChangedEvent, HostParametersChangedEvent, HostDeletedEvent, HostCreatedEvent}
import scala.concurrent.Future
import scala.util.{Failure, Success}
import org.jwebconsole.server.util.ErrorMessages

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 11:50
 */
class OperatingSystemViewActor(val dao: OperatingSystemDao) extends Actor with ActorLogging with Stash with ReadModelReplayingActor {
  def filterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostDataChangedEvent => true
    case ev: HostDeletedEvent => true
    case _ => false
  }

  def afterRecover(): Unit = {
    log.info("OperatingSystemView recovered")
  }

  def persistReplay(ev: AppEvent): Unit = ev match {
    case ev: HostDataChangedEvent => dao.refreshOperatingSystemInfo(ev.id, ev.data.osData)
    case ev: HostDeletedEvent => dao.deleteOperatingSystemInfo(ev.id)
  }

  def receive: Receive = {
    case ev: HostDataChangedEvent => futurePersist(persistReplay(ev))
    case ev: HostDeletedEvent => futurePersist(persistReplay(ev))
    case req: OperatingSystemInfoRequest => makeResponse(dao.getOperatingSystemInfo(req.hostId))
  }

  def makeResponse[T](daoAction: => T): Unit = {
    val current = sender()
    Future(daoAction) onComplete {
      case Success(v) =>
        current ! ResponseMessage(body = Some(v))
      case Failure(e) =>
        log.error(e, "Unable to request DB")
        current ! ResponseMessage(error = Some(ErrorMessages.DbConnectionFailureMessage))
    }
  }

}

case class OperatingSystemInfoRequest(hostId: String)