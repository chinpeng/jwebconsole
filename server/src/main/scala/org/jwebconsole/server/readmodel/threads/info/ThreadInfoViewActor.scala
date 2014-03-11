package org.jwebconsole.server.readmodel.threads.info

import org.jwebconsole.server.readmodel.common.{ReadModelRequest, ReadModelActor}
import org.jwebconsole.server.context.common.AppEvent
import org.jwebconsole.server.context.host.{HostDeletedEvent, HostDataChangedEvent}

class ThreadInfoViewActor(val dao: ThreadInfoDao) extends ReadModelActor {

  override def processRequest: PartialFunction[ReadModelRequest, Any] = {
    case req: ThreadListRequest => dao.threadNamesList(req.hostId)
  }

  override def persistEvent: PartialFunction[AppEvent, Unit] = {
    case ev: HostDataChangedEvent =>
      dao.refreshThreadInfo(ev.id, ev.data.threadData)
    case ev: HostDeletedEvent =>
      dao.deleteThreadInfo(ev.id)
  }

}

case class ThreadListRequest(hostId: String) extends ReadModelRequest





