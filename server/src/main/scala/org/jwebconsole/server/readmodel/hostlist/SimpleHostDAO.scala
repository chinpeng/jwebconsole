package org.jwebconsole.server.readmodel.hostlist

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import org.jwebconsole.server.readmodel.common.ReplayingDAO

class SimpleHostDAO(val db: Database) extends ReplayingDAO {

  val TableName = "all_hosts"

  class HostTable(tag: Tag) extends Table[(String, String, Int, Boolean)](tag, TableName) {

    def id = column[String]("id", O.PrimaryKey)

    def name = column[String]("host")

    def port = column[Int]("port")

    def connected = column[Boolean]("connected")

    def * = (id, name, port, connected)
  }

  def exists: Boolean =
    db withSession {
      implicit session =>
        !MTable.getTables(TableName).list().isEmpty
    }

  def putAll(hosts: List[SimpleHostView]): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        hosts foreach (host => hostQuery +=(host.id, host.name, host.port, host.connected))
    }
  }

  def updateParameters(host: SimpleHostView): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        val toUpdate = for (item: HostTable <- hostQuery if item.id === host.id) yield (item.id, item.name, item.port)
        toUpdate.update((host.id, host.name, host.port))
    }
  }

  def updateStatus(id: String, status: Boolean): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        val toUpdate = for (item: HostTable <- hostQuery if item.id === id) yield item.connected
        toUpdate.update(status)
    }
  }

  def create(host: SimpleHostView): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        hostQuery +=(host.id, host.name, host.port, host.connected)
    }
  }

  def delete(id: String): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        val toDelete = for (item: HostTable <- hostQuery if item.id === id) yield item
        toDelete.delete
    }
  }

  def getAll: List[SimpleHostView] = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        hostQuery.list().map(record => SimpleHostView(record._1, record._2, record._3, record._4))
    }
  }

  def getSingle: SimpleHostView = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        hostQuery.list().map(record => SimpleHostView(record._1, record._2, record._3, record._4)).head
    }
  }

  def createTable(): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        hostQuery.ddl.create
    }
  }

}
