package org.jwebconsole.server.readmodel.threads.count

import org.jwebconsole.server.readmodel.common.{ReadModelRequest, ReadModelActor}
import org.jwebconsole.server.context.common.AppEvent
import org.jwebconsole.server.context.host.{HostDeletedEvent, HostDataChangedEvent}

class ThreadCountViewActor(val dao: ThreadCountDao) extends ReadModelActor {

  override def processRequest: PartialFunction[ReadModelRequest, Any] = {
    case ThreadDataRequest(hostId) =>
      dao.getAllForHost(hostId)
    case ThreadDataLastNrRequest(hostId, number) =>
      dao.getLastNumberOfEntities(hostId, number)
  }

  override def persistEvent: PartialFunction[AppEvent, Unit] = {
    case ev: HostDataChangedEvent =>
      dao.addThreadCountRecord(ev.id, ev.data.threadData, ev.data.time)
    case HostDeletedEvent(hostId: String) =>
      dao.deleteHostRecord(hostId)
  }

}

case class ThreadDataRequest(hostId: String) extends ReadModelRequest

case class ThreadDataLastNrRequest(hostId: String, number: Int) extends ReadModelRequest
