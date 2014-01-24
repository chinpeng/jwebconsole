package org.jwebconsole.server.util

import akka.actor.ActorRef

trait CommonMessages

case object ValidationAck extends CommonMessages

case class ValidationFailed(messages: List[String]) extends CommonMessages

case class ValidationWithSender[T](source: ActorRef, validation: Validation[T]) extends CommonMessages

