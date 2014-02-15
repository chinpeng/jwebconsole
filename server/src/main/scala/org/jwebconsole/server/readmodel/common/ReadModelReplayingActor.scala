package org.jwebconsole.server.readmodel.common

import akka.actor.{Stash, ActorLogging, Props, Actor}
import org.jwebconsole.server.context.common.{ReplayFinished, AppEvent, EventStoreReplayingActor}
import org.jwebconsole.server.context.host.HostChangedEvent
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Try, Failure, Success}

trait ReadModelReplayingActor {
  this: Actor with ActorLogging with Stash =>

  implicit val executor = context.system.dispatcher

  override def preStart() {
    if (!dao.exists()) {
      context.become(replayingState)
      dao.createTable()
      makeReplay()
    } else {
      afterRecover()
    }
  }

  val replayingState: Receive = {
    case ev: AppEvent =>
      Try(persistReplay(ev)) match {
        case Success(_) => log.debug("Persisted replaying event:" + ev)
        case Failure(ex) => log.error(ex, "Unable to persist event: " + ev)
      }
    case ReplayFinished =>
      context.unbecome()
      unstashAll()
      afterRecover()
      log.debug("Finished replaying state, back to normal")
    case _ =>
      log.debug("Stashing event")
      stash()

  }

  def futurePersist(action: => Unit) {
    Future(action) onComplete {
      case Success(v) => log.debug("Successfully persisted event:" + v)
      case Failure(e) => log.error(e, "Unable to persist event")
    }
  }

  def makeReplay(): Unit = {
    context.actorOf(Props(new EventStoreReplayingActor(filterFunc, self)))
  }

  def dao: ReplayingDAO

  def filterFunc: PartialFunction[AppEvent, Boolean]

  def afterRecover(): Unit

  def persistReplay(ev: AppEvent)

}
