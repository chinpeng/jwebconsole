package org.jwebconsole.server.context.host

import akka.persistence.EventsourcedProcessor
import org.jwebconsole.server.util._
import org.jwebconsole.server.util.Valid
import org.jwebconsole.server.util.Invalid
import org.jwebconsole.server.util.ValidationConstants._
import akka.actor.{ActorLogging, ActorRef}
import org.jwebconsole.server.context.common.ResponseMessage
import org.jwebconsole.server.context.host.model.SimpleHostView

class HostARActor(override val processorId: String, validator: ActorRef) extends EventsourcedProcessor with ActorLogging {

  var model = HostStateModel()

  context.system.dispatcher

  def receiveRecover: Receive = {
    case ev => model = model.on(ev)
  }

  def receiveCommand: Receive = {
    case WithSender(source, cmd) =>
      log.info("received command message to Host AR: " + cmd)
      validator ! ValidateHost(model, cmd)
      context.become(validationState(source, cmd))
    case msg =>
      log.warning("Received Unknown message to Host Aggregate Root" + msg)
  }

  def validationState(sourceSender: ActorRef, cmd: HostCommand): Receive = {
    case Valid(body) =>
      becomeDefaultWith(() => processSuccessfulResponse(sourceSender, cmd))
    case Invalid(body, messages) =>
      processInvalidResponse(sourceSender, messages)
      becomeDefaultWith(() => processInvalidResponse(sourceSender, messages))
    case _ => stash()
  }

  def becomeDefaultWith(action: () => Unit): Unit = {
    action()
    context.unbecome()
    unstashAll()
  }

  def processSuccessfulResponse(source: ActorRef, cmd: HostCommand): Unit = {
    persist(eventFromCommand(cmd)) {
      ev =>
        model = model.on(ev)
        source ! ResponseMessage(body = Some(SimpleHostView(model.id, model.name, model.port)))
        context.system.eventStream.publish(ev)
    }

  }

  def eventFromCommand(cmd: HostCommand): HostChangedEvent = {
    cmd match {
      case CreateHostCommand(id, name, port, user, password) =>
        HostCreatedEvent(id, name, port, user, password)
      case ChangeHostCommand(id, name, port, user, password) =>
        HostParametersChangedEvent(id, name, port, user, password)
      case DeleteHostCommand(id) =>
        HostDeletedEvent(id)
    }
  }

  def processInvalidResponse(source: ActorRef, messages: List[InvalidMessage]): Unit = {
    source ! ResponseMessage(messages = Some(messages))
  }

}
