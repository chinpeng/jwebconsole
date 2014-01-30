package org.jwebconsole.server.context.host.model

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

class SimpleHostDAO(db: Database) {

  val TableName = "all_hosts"

  class HostTable(tag: Tag) extends Table[(String, String, Int)](tag, TableName) {

    def id = column[String]("id", O.PrimaryKey)

    def name = column[String]("host")

    def port = column[Int]("port")

    def * = (id, name, port)
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
        hosts foreach (host => hostQuery +=(host.id, host.name, host.port))
    }
  }

  def update(host: SimpleHostView): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        val toUpdate = for (item: HostTable <- hostQuery if item.id === host.id) yield item
        toUpdate.update((host.id, host.name, host.port))
    }
  }

  def create(host: SimpleHostView): Unit = {
    db withSession {
      implicit session =>
        val hostQuery = TableQuery[HostTable]
        hostQuery +=(host.id, host.name, host.port)
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
        hostQuery.list().map(record => SimpleHostView(record._1, record._2, record._3))
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
