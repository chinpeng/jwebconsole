package org.jwebconsole.server.context.common

import akka.persistence.EventsourcedProcessor
import akka.actor.{ActorLogging, ActorRef}
import scala.concurrent.duration._
import akka.util.Timeout
import org.jwebconsole.server.util.AppConstants

class EventStoreReplayingActor(filter: PartialFunction[AppEvent, Boolean], receiver: ActorRef, interval: FiniteDuration = 1.second) extends EventsourcedProcessor with ActorLogging {

  implicit val timeout = Timeout(AppConstants.DefaultTimeout)
  implicit val exec = context.system.dispatcher

  override val processorId = AppConstants.GlobalEventStoreProcessorId

  val cancel = context.system.scheduler.schedule(interval, interval) {
    self ! CheckReplayStatus
  }

  def receiveRecover: Receive = {
    case event: AppEvent if filter(event) =>
      receiver ! event
  }

  def receiveCommand: Receive = {
    case CheckReplayStatus if !cancel.isCancelled =>
      if (!recoveryRunning) {
        log.debug("Firing replay Finished")
        cancel.cancel()
        receiver ! ReplayFinished
        context.stop(self)
      }
    case CheckReplayStatus if cancel.isCancelled =>
      log.info("Trying to cancel already cancelled timer")
    case other =>
      log.warning("Received unhandled message for Event Store Replaying actor: " + other)
  }

  case object CheckReplayStatus


}
