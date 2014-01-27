package org.jwebconsole.server.context.util

import akka.actor.ActorRef
import org.jwebconsole.server.util.{InvalidMessage, Validation}

trait CommonMessages

trait AppEvent extends CommonMessages

case object ValidationAck extends CommonMessages

case class ValidationFailed(messages: List[InvalidMessage]) extends CommonMessages

case class ValidationWithSender[T](source: ActorRef, validation: Validation[T]) extends CommonMessages

case object GrabEvents extends CommonMessages

case class GrabbedEvents(events: List[AppEvent]) extends CommonMessages


