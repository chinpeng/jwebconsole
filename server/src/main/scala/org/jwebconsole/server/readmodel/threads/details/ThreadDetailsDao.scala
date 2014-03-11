package org.jwebconsole.server.readmodel.threads.details

import org.jwebconsole.server.readmodel.common.ReplayingDao
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import org.jwebconsole.server.context.host.AvailableThread

class ThreadDetailsDao(private val db: Database) extends ReplayingDao {

  val TableName = "thread_Details"

  case class ThreadDetailRow(id: Int = -1,
                             hostId: String,
                             threadId: Long,
                             stackTraceElement: String)

  case class ThreadDetailTable(tag: Tag) extends Table[ThreadDetailRow](tag, TableName) {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def hostId = column[String]("hostId")

    def threadId = column[Long]("threadId")

    def stackTraceElement = column[String]("stackTraceElement")

    override def * = (id, hostId, threadId, stackTraceElement) <>(ThreadDetailRow.tupled, ThreadDetailRow.unapply)

  }

  val threadDetailQuery = TableQuery[ThreadDetailTable]

  override def exists(): Boolean = {
    db withLockedSession {
      implicit session =>
        !MTable.getTables(TableName).list().isEmpty
    }
  }

  override def createTable(): Unit = {
    db withLockedSession {
      implicit session =>
        threadDetailQuery.ddl.create
    }
  }

  def refreshRecordsForHost(hostId: String, threads: List[AvailableThread]) {
    db withLockedSession {
      implicit session =>
        deleteRecordsForHost(hostId)
        val traces = for (detail <- threads; trace <- detail.stackTrace) yield (detail, trace)
        traces foreach {
          trace =>
            threadDetailQuery += ThreadDetailRow(
              hostId = hostId,
              threadId = trace._1.id,
              stackTraceElement = trace._2
            )
        }
    }
  }

  def getRecordsForThread(hostId: String, threadId: Long): List[ThreadDetailRow] = {
    db withLockedSession {
      implicit session =>
        threadDetailQuery
          .filter(_.hostId === hostId)
          .filter(_.threadId === threadId)
          .list()
    }
  }

  def deleteRecordsForHost(hostId: String) {
    db withLockedSession {
      implicit session =>
        recordsForHostId(hostId).delete
    }
  }

  private def recordsForHostId(hostId: String): Query[ThreadDetailTable, ThreadDetailRow] = {
    for (record <- threadDetailQuery if record.hostId === hostId) yield record
  }

}
