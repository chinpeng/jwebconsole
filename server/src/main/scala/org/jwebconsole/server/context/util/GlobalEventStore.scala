package org.jwebconsole.server.context.util

import akka.persistence.{Recover, EventsourcedProcessor}
import akka.actor.{Cancellable, ActorLogging, ActorRef}
import scala.concurrent.duration._
import akka.util.Timeout
import org.jwebconsole.server.util.AppConstants

class GlobalEventStore(filter: PartialFunction[AppEvent, Boolean], receiver: Option[ActorRef] = None)
  extends EventsourcedProcessor
  with ActorLogging {

  implicit val timeout = Timeout(AppConstants.DefaultTimeout)
  implicit val exec = context.system.dispatcher

  val interval = 5 seconds

  val cancel: Option[Cancellable] = receiver match {
    case None => None
    case Some(v) => Some(context.system.scheduler.schedule(interval, interval) {
      self ! CheckReplayStatus
    })
  }

  override def preStart(): Unit = {
    if (receiver.isDefined) self ! Recover()
  }

  override val processorId = "global-event-store"

  def receiveReplay: Receive = {
    case event: AppEvent =>
      if (filter(event)) receiver match {
        case None => Unit
        case Some(ref) => ref ! event
      }
  }

  def receiveCommand: Receive = {
    case event: AppEvent =>
      persist(event) {
        ev => Unit
      }
    case CheckReplayStatus if cancel.get.isCancelled =>
      Unit
    case CheckReplayStatus if !cancel.get.isCancelled =>
      if (recoveryFinished) cancel.map(_.cancel())
      receiver.map(_ ! ReplayFinished)
    case _ =>
      log.warning("Received unknown message in global event store")
  }

  case object CheckReplayStatus

}
