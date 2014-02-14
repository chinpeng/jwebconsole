package org.jwebconsole.server.context.host

import akka.persistence.EventsourcedProcessor
import org.jwebconsole.server.util._
import org.jwebconsole.server.util.Valid
import org.jwebconsole.server.util.Invalid
import akka.actor.{ActorLogging, ActorRef}
import org.jwebconsole.server.context.common.ResponseMessage
import org.jwebconsole.server.readmodel.hostlist.SimpleHostView

class HostARActor(override val processorId: String, validator: ActorRef) extends EventsourcedProcessor with ActorLogging {

  var model = HostStateModel()

  context.system.dispatcher

  def receiveRecover: Receive = {
    case ev =>
      log.debug("Receiving replay: " + ev)
      model = model.on(ev)
  }

  def receiveCommand: Receive = {
    receiveWorkerCommand orElse receiveUserCommand
  }

  def receiveWorkerCommand: Receive = {
    case cmd: ChangeHostDataCommand =>
      persist(HostDataChangedEvent(cmd.id, cmd.data)) {
        ev =>
          log.debug("Successfully Persisted event: " + ev)
          model = model.on(ev)
          context.system.eventStream.publish(ev)
      }
  }

  def receiveUserCommand: Receive = {
    case WithSender(source, cmd) =>
      log.debug("received command message to Host AR: " + cmd)
      validator ! ValidateHost(model, cmd)
      context.become(validationState(source, cmd))
    case msg =>
      log.warning("Received Unknown message to Host Aggregate Root" + msg)
  }

  def validationState(sourceSender: ActorRef, cmd: HostCommand): Receive = {
    case Valid(body) =>
      becomeDefaultWith(processSuccessfulResponse(sourceSender, cmd))
    case Invalid(body, messages) =>
      becomeDefaultWith(processInvalidResponse(sourceSender, messages))
    case _ =>
      log.debug("Stashing events while waiting for validation")
      stash()
  }

  def becomeDefaultWith(action: => Unit): Unit = {
    log.debug("Received validation response. Back to normal behaviour.")
    action
    context.unbecome()
    unstashAll()
  }

  def processSuccessfulResponse(source: ActorRef, cmd: HostCommand): Unit = {
    val current = source
    persist(eventFromCommand(cmd)) {
      ev =>
        model = model.on(ev)
        current ! ResponseMessage(body = Some(SimpleHostView(model.id, model.name, model.port)))
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
    log.debug("Command is invalid. Sending invalid response back to client")
    source ! ResponseMessage(messages = Some(messages))
  }


}
