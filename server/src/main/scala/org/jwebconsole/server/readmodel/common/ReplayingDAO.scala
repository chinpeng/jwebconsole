package org.jwebconsole.server.readmodel.common


trait ReplayingDAO {

  def createTable(): Unit

  def exists(): Boolean

}
