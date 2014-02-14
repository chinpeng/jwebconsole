package org.jwebconsole.server.readmodel.threads

import org.jwebconsole.server.readmodel.common.ReplayingDAO
import org.jwebconsole.server.context.host.ThreadData

class ThreadDataDAO extends ReplayingDAO {

  def createTable(): Unit = {

  }

  def exists(): Boolean = {
    false
  }

  def addThreadDataRecord(hostId: String, threadData: ThreadData): Unit = {

  }

}
