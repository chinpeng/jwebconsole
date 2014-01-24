package org.jwebconsole.server.actor.common

import akka.actor.{ActorLogging, Actor}
import org.jwebconsole.server.util._
import org.jwebconsole.server.util.ValidationWithSender
import org.jwebconsole.server.util.Valid
import org.jwebconsole.server.util.Invalid

class ValidationActor extends Actor with ActorLogging {

  def receive: Receive = {
    case ValidationWithSender(source, validation) =>
      validation match {
        case Valid(v) =>
          log.debug("Validation success for " + v)
          source ! ValidationAck
        case Invalid(v, messages) =>
          log.debug("Validation failed for " + v + ". Messages: " + messages.mkString(" | "))
          source ! ValidationFailed(messages)
      }
    case other =>
      log.warning("Unknown message " + other + " was received by Validation actor")
  }

}
