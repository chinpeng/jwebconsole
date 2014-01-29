package org.jwebconsole.server.context.util

import akka.persistence.{Recover, EventsourcedProcessor}
import akka.actor.{Cancellable, ActorLogging, ActorRef}
import scala.concurrent.duration._
import akka.util.Timeout
import org.jwebconsole.server.util.AppConstants

class EventStoreReplayingActor(filter: PartialFunction[AppEvent, Boolean], receiver: ActorRef) extends EventsourcedProcessor with ActorLogging {

  implicit val timeout = Timeout(AppConstants.DefaultTimeout)
  implicit val exec = context.system.dispatcher
  val interval = 5.seconds

  override val processorId = AppConstants.GlobalEventStoreProcessorId

  val cancel = context.system.scheduler.schedule(interval, interval) {
    self ! CheckReplayStatus
  }

  def receiveReplay: Receive = {
    case event: AppEvent =>
      receiver ! event
  }

  def receiveCommand: Receive = {
    case CheckReplayStatus if !cancel.isCancelled =>
      if (recoveryFinished) {
        receiver ! ReplayFinished
        cancel.cancel()
      }
    case CheckReplayStatus if cancel.isCancelled =>
      log.info("Trying to cancel already cancelled timer")
    case _ =>
      log.warning("Received unhandled message for Event Store Replaying actor")
  }

  case object CheckReplayStatus

}
