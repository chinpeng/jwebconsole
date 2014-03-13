package org.jwebconsole.server.readmodel.hostlist

import org.jwebconsole.server.util.{ErrorMessages, ErrorMessage, AppConstants}
import org.jwebconsole.server.context.common._
import org.jwebconsole.server.readmodel.common.{ReadModelRequest, ReadModelActor}
import scala.concurrent.Future
import org.jwebconsole.server.context.host.{HostDataChangedEvent, HostDeletedEvent, HostParametersChangedEvent, HostCreatedEvent}
import java.sql.SQLException

class HostListViewActor(val dao: SimpleHostDao) extends ReadModelActor {

  override def processRequest: PartialFunction[ReadModelRequest, Any] = {
    case SimpleHostViewListRequest =>
      dao.getAll.map(withoutCredentials)
    case SimpleHostViewRequest(id) =>
      withoutCredentials(dao.getSingle(id))
  }

  private def withoutCredentials(host: SimpleHostView): SimpleHostView = {
    host.copy(login = "", password = "")
  }

  override def persistEvent: PartialFunction[AppEvent, Unit] = {
    case ev: HostCreatedEvent =>
      dao.create(SimpleHostView(ev.id, ev.name, ev.port, connected = true))
    case ev: HostParametersChangedEvent =>
      dao.updateParameters(SimpleHostView(ev.id, ev.name, ev.port))
    case ev: HostDeletedEvent =>
      dao.delete(ev.id)
    case ev: HostDataChangedEvent =>
      dao.updateStatus(ev.id, ev.data.connected)
  }

  override def afterRecover(): Unit = {
    log.debug("Firing connections list event")
    Future(dao.getAll).map(items => context.system.eventStream.publish(AvailableHostsList(items)))
  }

  override def handleFailure: PartialFunction[(ReadModelRequest, Throwable), ErrorMessage] = {
    case (r, e) => e match {
      case e: SQLException => ErrorMessages.DbConnectionFailureMessage
      case e: NoSuchElementException => ErrorMessages.HostNotFoundMessage
      case _ => ErrorMessages.UnknownErrorMessage
    }
  }

}

case object SimpleHostViewListRequest extends ReadModelRequest

case class SimpleHostViewRequest(hostId: String) extends ReadModelRequest
