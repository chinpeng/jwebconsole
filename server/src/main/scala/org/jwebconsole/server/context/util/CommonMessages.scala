package org.jwebconsole.server.context.util

import akka.actor.ActorRef
import org.jwebconsole.server.util.{InvalidMessage, Validation}

trait AppEvent

trait CommonMessages

case class ResponseMessage(body: Option[Any] = None, messages: Option[List[InvalidMessage]] = None, error: Option[String] = None)

case class ValidationWithSender[T](source: ActorRef, validation: Validation[T]) extends CommonMessages

case object ReplayFinished extends CommonMessages
