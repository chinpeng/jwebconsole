package org.jwebconsole.server.context.util

import akka.persistence.{Recover, EventsourcedProcessor}
import akka.actor.ActorLogging
import org.jwebconsole.server.util.AppConstants

class GlobalEventStore extends EventsourcedProcessor with ActorLogging {

  override val processorId = AppConstants.GlobalEventStoreProcessorId

  override def preStart() {
    self ! Recover(toSequenceNr = 0)
  }

  def receiveReplay: Receive = {
    case ev => log.warning("Received unknown message during replay of global event store")
  }

  def receiveCommand: Receive = {
    case event: AppEvent =>
      persist(event) {
        ev =>
          log.debug("persisted event" + ev)
      }
  }

}
