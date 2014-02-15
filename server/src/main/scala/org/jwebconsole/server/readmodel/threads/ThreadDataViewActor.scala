package org.jwebconsole.server.readmodel.threads

import akka.actor.{Actor, ActorLogging, Stash}
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor
import org.jwebconsole.server.context.common.{ResponseMessage, AppEvent}
import org.jwebconsole.server.context.host.HostDataChangedEvent
import scala.concurrent.Future
import scala.util.{Failure, Success}

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

  def makeResponse(hostId: String): Unit = {
    val current = sender()
    Future(dao.getAllForHost(hostId)) onComplete {
      case Success(v) => current ! ResponseMessage(body = Some(v))
      case Failure(e) => current ! ResponseMessage(error = Some(e.getMessage))
    }
  }

  def receive: Receive = {
    case ev: HostDataChangedEvent =>
      persistAsync(ev)
    case ThreadDataRequest(hostId) =>
      makeResponse(hostId)
  }

}

case class ThreadDataRequest(hostId: String)
