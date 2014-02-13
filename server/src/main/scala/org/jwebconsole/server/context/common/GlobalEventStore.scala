package org.jwebconsole.server.context.common

import akka.persistence.{Recover, EventsourcedProcessor}
import akka.actor.ActorLogging
import org.jwebconsole.server.util.AppConstants

class GlobalEventStore extends EventsourcedProcessor with ActorLogging {

  override val processorId = AppConstants.GlobalEventStoreProcessorId

  override def preStart() {
    self ! Recover(toSequenceNr = 0)
  }

  def receiveRecover: Receive ={
    case ev => log.warning("Received unknown message during replay of global event store")
  }

  def receiveCommand: Receive = {
    case event: AppEvent =>
      persist(event) {
        ev =>
          log.debug("persisted event to event store: " + ev)
      }
    case other =>
      log.warning("Unknown message was delivered to global event store: " + other)
  }


}
