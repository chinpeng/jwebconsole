package org.jwebconsole.server.context.util

import akka.persistence.EventsourcedProcessor
import scala.collection.mutable.ListBuffer

class AppEventHandler(filter: PartialFunction[AppEvent, Boolean]) extends EventsourcedProcessor {

  override val processorId = "app-event-handler"

  val watchingEvents = ListBuffer.empty[AppEvent]

  def receiveReplay: Receive = {
    case event: AppEvent =>
      if (filter(event)) watchingEvents += event
  }

  def receiveCommand: Receive = {
    case event: AppEvent =>
      persist(event) {
        ev => Unit
      }
    case GrabEvents =>
      sender ! GrabbedEvents(watchingEvents.toList)
  }
}
