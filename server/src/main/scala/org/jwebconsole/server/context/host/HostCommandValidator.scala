package org.jwebconsole.server.context.host

import org.jwebconsole.server.util.{TimeoutFuture, Valid, Invalid, Validation}
import akka.actor.{ActorLogging, Actor}
import org.jwebconsole.server.util.ValidationConstants._
import scala.Some
import org.jwebconsole.server.jmx.JMXConnectionChecker
import scala.util.{Failure, Success}
import scala.concurrent.duration._

class HostCommandValidator(connectionChecker: JMXConnectionChecker) extends Actor with ActorLogging {

  implicit val executionContext = context.system.dispatcher
  implicit val system = context.system

  def receive: Receive = {
    case ValidateHost(model, command) => validate(model, command)
    case _ => log.warning("Received unknown message to Host Command Validator")
  }

  def validateCreateHostCommand(model: HostStateModel, cmd: CreateHostCommand): Unit = {
    if (!model.deleted) sender ! Invalid(model, List(HostAlreadyCreated))
    else validateChanges(model, cmd)
  }

  def modelFromCommand(source: HostStateModel, cmd: HostCommand): HostStateModel = cmd match {
    case CreateHostCommand(id, name, port, user, password) =>
      source.on(HostCreatedEvent(id, name, port, user, password))
    case ChangeHostCommand(id, name, port, user, password) =>
      source.on(HostParametersChangedEvent(id, name, port, user, password))
    case DeleteHostCommand(id) =>
      source.on(HostDeletedEvent(id))
  }

  def validateChanges(model: HostStateModel, cmd: HostCommand): Unit = {
    val changed = modelFromCommand(model, cmd)
    val validationResult = for (validPort <- validatePort(changed);
                                validHost <- validateHost(validPort)) yield validHost
    validationResult match {
      case Invalid(body, msg) =>
        sender ! Invalid(body, msg)
      case Valid(body) =>
        checkHostConnection(changed)
    }
  }

  def checkHostConnection(model: HostStateModel): Unit = {
    val currentSender = sender()
    TimeoutFuture(3 seconds, connectionChecker.checkConnection(model.name, model.port)) onComplete {
      case Success(v) => currentSender ! Valid(model)
      case Failure(e) =>
        log.error("Unable to connect to target host: " + model)
        currentSender ! Invalid(model, List(UnableToConnectMessage))
    }
  }

  def validateChangeHostCommand(model: HostStateModel, cmd: ChangeHostCommand): Unit = {
    if (model.deleted) sender ! Invalid(model, List(HostDeletedMessage))
    else validateChanges(model, cmd)
  }

  def validateDeleteHostCommand(model: HostStateModel, cmd: DeleteHostCommand): Unit = {
    if (model.deleted) sender ! Invalid(model, List(HostDeletedMessage))
    else sender ! Valid(model)
  }

  def validate(model: HostStateModel, command: HostCommand): Unit = command match {
    case cmd: CreateHostCommand =>
      validateCreateHostCommand(model, cmd)
    case cmd: ChangeHostCommand =>
      validateChangeHostCommand(model, cmd)
    case cmd: DeleteHostCommand =>
      validateDeleteHostCommand(model, cmd)
    case other =>
      sender ! Invalid(model, List(UnknownErrorMessage))
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

case class ValidateHost(model: HostStateModel, command: HostCommand)