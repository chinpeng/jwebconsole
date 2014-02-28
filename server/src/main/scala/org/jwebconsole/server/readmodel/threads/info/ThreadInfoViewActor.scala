package org.jwebconsole.server.readmodel.threads.info

import akka.actor.{Stash, ActorLogging, Actor}
import org.jwebconsole.server.readmodel.threads._
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor
import org.jwebconsole.server.context.common.{ResponseMessage, AppEvent}
import org.jwebconsole.server.context.host.{HostDeletedEvent, HostDataChangedEvent}
import scala.concurrent.Future
import org.jwebconsole.server.util.ErrorMessages
import scala.util.{Success, Failure}

class ThreadInfoViewActor(val dao: ThreadInfoDao) extends Actor with ActorLogging with Stash with ReadModelReplayingActor {

  override def receive: Receive = {
    case ev: HostDataChangedEvent =>
      persistAsync(ev)
    case ev: HostDeletedEvent =>
      persistAsync(ev)
    case request: ThreadListRequest =>
      respondAsync(dao.threadNamesList(request.hostId))
  }

  def respondAsync[T](action: => T): Unit = {
    val current = sender()
    Future(action) onComplete {
      case Success(v) => current ! ResponseMessage(body = Some(v))
      case Failure(e) =>
        log.error("Cannot respond to client: ", e)
        current ! ResponseMessage(error = Some(ErrorMessages.UnknownErrorMessage))
    }
  }

  def persistAsync(event: AppEvent): Unit = {
    Future(persistReplay(event)) onComplete {
      case Failure(ex) => log.error(ex, "Failed to persist event: " + event)
      case Success(ev) => log.debug("Persisted event : " + ev)
    }
  }

  override def persistReplay(ev: AppEvent): Unit = ev match {
    case ev: HostDataChangedEvent =>
      dao.refreshThreadInfo(ev.id, ev.data.threadData)
    case ev: HostDeletedEvent =>
      dao.deleteThreadInfo(ev.id)
    case other =>
      log.warning("Unknown message during replaying" + other)
  }

  override def afterRecover(): Unit = {
    log.info("Thread Info View recover completed")
  }

  def filterFunc = commonThreadFilterFunc

}

case class ThreadListRequest(hostId: String)





