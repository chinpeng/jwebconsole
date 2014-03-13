package org.jwebconsole.server.readmodel.hostlist

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import org.jwebconsole.server.readmodel.common.ReplayingDao
import java.sql.SQLException

class SimpleHostDao(val db: Database) extends ReplayingDao {

  val TableName = "all_hosts"

  class HostTable(tag: Tag) extends Table[SimpleHostView](tag, TableName) {

    def id = column[String]("id", O.PrimaryKey)

    def name = column[String]("host")

    def port = column[Int]("port")

    def login = column[String]("login")

    def password = column[String]("password")

    def connected = column[Boolean]("connected")

    def * = (id, name, port, login, password, connected) <> (SimpleHostView.tupled, SimpleHostView.unapply)
  }

  private val hostQuery = TableQuery[HostTable]

  def exists(): Boolean =
    db withLockedSession {
      implicit session =>
        !MTable.getTables(TableName).list().isEmpty
    }

  def putAll(hosts: List[SimpleHostView]): Unit = {
    db withLockedSession {
      implicit session =>
        hosts foreach (hostQuery += _)
    }
  }

  def updateParameters(host: SimpleHostView): Unit = {
    db withLockedSession {
      implicit session =>
        val toUpdate = for (item: HostTable <- hostQuery if item.id === host.id) yield (item.id, item.name, item.port)
        toUpdate.update((host.id, host.name, host.port))
    }
  }

  def updateStatus(id: String, status: Boolean): Unit = {
    db withLockedSession {
      implicit session =>
        val toUpdate = for (item: HostTable <- hostQuery if item.id === id) yield item.connected
        toUpdate.update(status)
    }
  }

  def create(host: SimpleHostView): Unit = {
    db withLockedSession {
      implicit session =>
        hostQuery += host
    }
  }

  def delete(id: String): Unit = {
    db withLockedSession {
      implicit session =>
        val toDelete = for (item: HostTable <- hostQuery if item.id === id) yield item
        toDelete.delete
    }
  }

  @throws(classOf[SQLException])
  def getAll: List[SimpleHostView] = {
    db withLockedSession {
      implicit session =>
        hostQuery.list()
    }
  }

  def getSingle(hostId: String): SimpleHostView = {
    db withLockedSession {
      implicit session =>
        val result = for (item: HostTable <- hostQuery if item.id === hostId) yield item
        result.list().head
    }
  }

  def createTable(): Unit = {
    db withLockedSession {
      implicit session =>
        hostQuery.ddl.create
    }
  }

}
