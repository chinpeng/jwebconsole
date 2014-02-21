package org.jwebconsole.server.readmodel.threads

import org.jwebconsole.server.readmodel.common.ReplayingDAO
import org.jwebconsole.server.context.host.ThreadData
import scala.slick.driver.H2Driver.simple._
import java.sql.Timestamp
import scala.slick.jdbc.meta.MTable

class ThreadDataDAO(val db: Database) extends ReplayingDAO {

  val TableName = "thread_data_table"

  case class ThreadDataRow(id: Int, hostId: String, time: Timestamp, threadCount: Int, peakThreadCount: Int)

  class ThreadDataTable(tag: Tag) extends Table[ThreadDataRow](tag, TableName) {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def hostId = column[String]("hostId")

    def time = column[Timestamp]("time")

    def threadCount = column[Int]("threadCount")

    def peakThreadCount = column[Int]("peakThreadCount")

    def * = (id, hostId, time, threadCount, peakThreadCount) <>(ThreadDataRow.tupled, ThreadDataRow.unapply)
  }

  val threadDataQuery = TableQuery[ThreadDataTable]


  def createTable(): Unit = {
    db withSession {
      implicit session =>
        threadDataQuery.ddl.create
    }
  }

  def exists(): Boolean = {
    db withSession {
      implicit session =>
        !MTable.getTables(TableName).list().isEmpty
    }
  }

  def addThreadDataRecord(hostId: String, threadData: ThreadData): Unit = {
    db withSession {
      implicit session =>
        val time = System.currentTimeMillis()
        val row = ThreadDataRow(0, hostId, new Timestamp(time), threadData.threadCount, threadData.peakThreadCount)
        threadDataQuery += row
    }
  }

  def getAllForHost(hostId: String): List[ThreadDataRow] = {
    db withSession {
      implicit session =>
        val res = for (row <- threadDataQuery if row.hostId === hostId) yield row
        res.list()
    }
  }

  def getLastNumberOfEntities(hostId: String, bound: Int): List[ThreadDataRow] = {
    db withSession {
      implicit session =>
        threadDataQuery
          .filter(_.hostId === hostId)
          .sortBy(_.time.desc)
          .take(bound)
          .list()
    }
  }

}
