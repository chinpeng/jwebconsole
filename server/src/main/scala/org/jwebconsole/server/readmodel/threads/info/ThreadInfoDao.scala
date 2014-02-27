package org.jwebconsole.server.readmodel.threads.info

import org.jwebconsole.server.readmodel.common.ReplayingDAO

class ThreadInfoDao extends ReplayingDAO {

  override def exists(): Boolean = {
    false
  }

  override def createTable(): Unit = {

  }

}
