package org.jwebconsole.server.readmodel.threads.details

import org.jwebconsole.server.readmodel.common.{ReadModelRequest, ReadModelActor}
import org.jwebconsole.server.context.common.AppEvent
import org.jwebconsole.server.context.host.{HostDataChangedEvent, HostDeletedEvent}

class ThreadDetailsViewActor(val dao: ThreadDetailsDao) extends ReadModelActor {

  override def processRequest: PartialFunction[ReadModelRequest, Any] = {
    case req: ThreadDetailsRequest =>
      dao.getRecordsForThread(req.hostId, req.threadId)
  }

  override def persistEvent: PartialFunction[AppEvent, Unit] = {
    case ev: HostDeletedEvent =>
      dao.deleteRecordsForHost(ev.id)
    case ev: HostDataChangedEvent =>
      dao.refreshRecordsForHost(ev.id, ev.data.threadData.availableThreads)
  }

}

case class ThreadDetailsRequest(hostId: String, threadId: Long) extends ReadModelRequest

