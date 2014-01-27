package org.jwebconsole.server.context.host

import akka.persistence.EventsourcedProcessor
import org.jwebconsole.server.util._
import org.jwebconsole.server.util.Valid
import org.jwebconsole.server.util.Invalid
import org.jwebconsole.server.util.ValidationConstants._
import akka.actor.ActorRef
import org.jwebconsole.server.context.util.ValidationWithSender

class HostARActor(override val processorId: String) extends EventsourcedProcessor {

  var model = HostStateModel()

  def receiveReplay: Receive = {
    case ev => model = model.on(ev)
  }

  def receiveCommand: Receive = {
    case WithSender(source, cmd) =>
      val (newModel, ev) = createValidationModel(cmd)
      validate(newModel, ev, source)
  }


  def validate(newModel: HostStateModel, ev: HostChangedEvent, source: ActorRef) {
    validate(newModel) match {
      case valid: Valid => persist(ev) {
        ev =>
          model = newModel
          context.system.eventStream.publish(ValidationWithSender(source, valid))
      }
      case invalid: Invalid =>
        context.system.eventStream.publish(ValidationWithSender(source, invalid))
    }
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
    Option(item.port) match {
      case None =>
        Invalid(item, List(PortEmptyMessage))
      case Some(port) => port match {
        case num if num < 0 => Invalid(item, List(PortMustBePositive))
        case num if num > 100000 => Invalid(item, List(BigNumberForPort))
        case _ => Valid(item)
      }
    }
  }

}
