package org.jwebconsole.server.context.host

import akka.persistence.EventsourcedProcessor
import org.jwebconsole.server.util._
import org.jwebconsole.server.util.Valid
import org.jwebconsole.server.util.Invalid
import org.jwebconsole.server.util.ValidationConstants._
import akka.actor.ActorRef
import org.jwebconsole.server.context.common.ResponseMessage
import org.jwebconsole.server.context.host.model.SimpleHostView

class HostARActor(override val processorId: String) extends EventsourcedProcessor {

  var model = HostStateModel()

  def receiveRecover: Receive = {
    case ev => model = model.on(ev)
  }

  def receiveCommand: Receive = {
    case WithSender(source, cmd) =>
      val (newModel, ev) = createValidationModel(cmd)
      validate(newModel, ev, source)
  }


  def validate(newModel: HostStateModel, ev: HostChangedEvent, source: ActorRef) {
    val isValid = for (validChanges <- validate(newModel);
                       validEvent <- validateEvent(validChanges, ev)) yield validEvent
    isValid match {
      case valid@Valid(_) => persist(ev) {
        ev =>
          model = newModel
          source ! ResponseMessage(body = Some(SimpleHostView(model.id, model.name, model.port)))
          context.system.eventStream.publish(ev)
      }
      case invalid@Invalid(_, _) =>
        source ! ResponseMessage(messages = Some(invalid.messages))
    }
  }

  def validateEvent(newModel: HostStateModel, event: HostChangedEvent): Validation[HostStateModel] = event match {
    case ev: HostCreatedEvent if !model.deleted =>
      Invalid(newModel, List(HostAlreadyCreated))
    case ev: HostParametersChangedEvent if model.deleted =>
      Invalid(newModel, List(HostDeletedMessage))
    case ev: HostDeletedEvent if model.deleted =>
      Invalid(newModel, List(HostDeletedMessage))
    case _ =>
      Valid(newModel)
  }

  def createValidationModel(cmd: HostCommand): (HostStateModel, HostChangedEvent) = {
    cmd match {
      case CreateHostCommand(id, name, port, user, password) =>
        val event = HostCreatedEvent(id, name, port, user, password)
        (model.on(event), event)
      case ChangeHostCommand(id, name, port, user, password) =>
        val event = HostParametersChangedEvent(id, name, port, user, password)
        (model.on(event), event)
      case DeleteHostCommand(id) =>
        val event = HostDeletedEvent(id)
        (model.on(event), event)
    }
  }

  def validate(item: HostStateModel): Validation[HostStateModel] = {
    for (validHost <- validateHost(item);
         validPort <- validatePort(item)
    ) yield validPort
  }

  def validateHost(item: HostStateModel): Validation[HostStateModel] = {
    Option(item.name) match {
      case None =>
        Invalid(item, List(HostEmptyMessage))
      case Some(v) => v match {
        case name if name == "" || name.trim == "" => Invalid(item, List(HostEmptyMessage))
        case _ => Valid(item)
      }
    }
  }

  def validatePort(item: HostStateModel): Validation[HostStateModel] = {
    item.port match {
      case num if num <= 0 => Invalid(item, List(PortMustBePositive))
      case num if num > 100000 => Invalid(item, List(BigNumberForPort))
      case _ => Valid(item)
    }
  }


}
