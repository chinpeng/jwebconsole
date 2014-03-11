package org.jwebconsole.server.readmodel.summary.os

import org.jwebconsole.server.readmodel.common.{ReadModelRequest, ReadModelActor}
import org.jwebconsole.server.context.common.AppEvent
import org.jwebconsole.server.context.host.{HostDataChangedEvent, HostDeletedEvent}

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 11:50
 */
class OperatingSystemViewActor(val dao: OperatingSystemDao) extends ReadModelActor {

  override def processRequest: PartialFunction[ReadModelRequest, Any] = {
    case req: OperatingSystemInfoRequest =>
      dao.getOperatingSystemInfo(req.hostId)
  }

  override def persistEvent: PartialFunction[AppEvent, Unit] = {
    case ev: HostDataChangedEvent =>
      dao.refreshOperatingSystemInfo(ev.id, ev.data.osData)
    case ev: HostDeletedEvent =>
      dao.deleteOperatingSystemInfo(ev.id)
  }
}

case class OperatingSystemInfoRequest(hostId: String) extends ReadModelRequest
