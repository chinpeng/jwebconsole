package org.jwebconsole.server.context.util

import akka.actor.ActorRef
import org.jwebconsole.server.util.{InvalidMessage, Validation}
import scala.util.Try

trait CommonMessages

trait AppEvent

case class ResponseMessage(payload: Any  = null, messages: List[InvalidMessage] = List.empty[InvalidMessage], error: String = "")

case class ValidationAck(payload: Any = null) extends CommonMessages

case class ValidationFailed(messages: List[InvalidMessage] = List.empty[InvalidMessage]) extends CommonMessages

case class ValidationWithSender[T](source: ActorRef, validation: Validation[T]) extends CommonMessages

case object ReplayFinished extends CommonMessages
