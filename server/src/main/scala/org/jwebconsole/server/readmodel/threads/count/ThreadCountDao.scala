package org.jwebconsole.server.readmodel.threads.count

import org.jwebconsole.server.readmodel.common.ReplayingDao
import org.jwebconsole.server.context.host.ThreadData
import scala.slick.driver.H2Driver.simple._
import java.sql.Timestamp
import scala.slick.jdbc.meta.MTable
import java.util.Date

class ThreadCountDao(private val db: Database) extends ReplayingDao {

  val TableName = "thread_data_table"

  case class ThreadCountRow(id: Int, hostId: String, time: Timestamp, threadCount: Int, peakThreadCount: Int)

  class ThreadCountTable(tag: Tag) extends Table[ThreadCountRow](tag, TableName) {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def hostId = column[String]("hostId")

    def time = column[Timestamp]("time")

    def threadCount = column[Int]("threadCount")

    def peakThreadCount = column[Int]("peakThreadCount")

    def * = (id, hostId, time, threadCount, peakThreadCount) <>(ThreadCountRow.tupled, ThreadCountRow.unapply)
  }

  val threadCountQuery = TableQuery[ThreadCountTable]


  def createTable(): Unit = {
    db withLockedSession {
      implicit session =>
        threadCountQuery.ddl.create
    }
  }

  def exists(): Boolean = {
    db withLockedSession {
      implicit session =>
        !MTable.getTables(TableName).list().isEmpty
    }
  }

  def addThreadCountRecord(hostId: String, threadData: ThreadData, time: Date): Unit = {
    db withLockedSession {
      implicit session =>
        val row = ThreadCountRow(0, hostId, new Timestamp(time.getTime), threadData.threadCount, threadData.peakThreadCount)
        threadCountQuery += row
    }
  }

  def getAllForHost(hostId: String): List[ThreadCountRow] = {
    db withLockedSession {
      implicit session =>
        val res = for (row <- threadCountQuery if row.hostId === hostId) yield row
        res.list()
    }
  }

  def getLastNumberOfEntities(hostId: String, bound: Int): List[ThreadCountRow] = {
    db withLockedSession {
      implicit session =>
        threadCountQuery
          .filter(_.hostId === hostId)
          .sortBy(_.time.desc.nullsFirst)
          .take(bound)
          .list()
          .sortBy(_.time.getTime)
    }
  }

  def deleteHostRecord(hostId: String): Unit = {
    db withLockedSession {
      implicit session =>
        threadCountQuery
          .filter(_.hostId === hostId)
          .delete
    }
  }

}
