package org.jwebconsole.server.readmodel.threads

import akka.actor.{Actor, ActorLogging, Stash}
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor
import org.jwebconsole.server.context.common.{ResponseMessage, AppEvent}
import org.jwebconsole.server.context.host.HostDataChangedEvent
import scala.concurrent.Future
import scala.util.{Failure, Success}
import org.jwebconsole.server.util.ErrorMessages

class ThreadDataViewActor(val dao: ThreadDataDAO) extends Actor with ActorLogging with Stash with ReadModelReplayingActor {

  def filterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostDataChangedEvent => true
    case _ => false
  }

  def afterRecover(): Unit = Unit

  def persistReplay(event: AppEvent): Unit = event match {
    case ev: HostDataChangedEvent =>
      dao.addThreadDataRecord(ev.id, ev.data.threadData)
  }

  def persistAsync(event: HostDataChangedEvent): Unit = {
    futurePersist(persistReplay(event))
  }

  def makeResponse[T](daoAction: => T): Unit = {
    val current = sender()
    Future(daoAction) onComplete {
      case Success(v) => current ! ResponseMessage(body = Some(v))
      case Failure(e) => current !
        log.error(e, "Unable to request DB")
        ResponseMessage(error = Some(ErrorMessages.DbConnectionFailureMessage))
    }
  }

  def receive: Receive = {
    case ev: HostDataChangedEvent =>
      persistAsync(ev)
    case ThreadDataRequest(hostId) =>
      makeResponse(dao.getAllForHost(hostId))
    case ThreadDataLastNrRequest(hostId, number) =>
      makeResponse(dao.getLastNumberOfEntities(hostId, number))
  }

}

case class ThreadDataRequest(hostId: String)

case class ThreadDataLastNrRequest(hostId: String, number: Int)
