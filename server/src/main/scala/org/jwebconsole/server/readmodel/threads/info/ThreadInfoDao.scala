package org.jwebconsole.server.readmodel.threads.info

import org.jwebconsole.server.readmodel.common.ReplayingDao
import org.jwebconsole.server.context.host.ThreadData
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

class ThreadInfoDao(private val db: Database) extends ReplayingDao {

  val ThreadNameTableName = "thread_name_table"

  case class ThreadNameRow(id: Int = -1, threadId: Long, hostId: String, name: String)

  case class ThreadNameTable(tag: Tag) extends Table[ThreadNameRow](tag, ThreadNameTableName) {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def threadId = column[Long]("threadId")

    def hostId = column[String]("hostId")

    def name = column[String]("name")

    def * = (id, threadId, hostId, name) <>(ThreadNameRow.tupled, ThreadNameRow.unapply)

  }

  val threadNameQuery = TableQuery[ThreadNameTable]

  override def exists(): Boolean = {
    db withLockedSession {
      implicit session =>
        !MTable.getTables(ThreadNameTableName).list().isEmpty
    }
  }

  override def createTable(): Unit = {
    db withLockedSession {
      implicit session =>
        threadNameQuery.ddl.create
    }
  }

  def refreshThreadInfo(hostId: String, threadData: ThreadData): Unit = {
    db withLockedSession {
      implicit session =>
        deleteThreadInfo(hostId)
        threadData.availableThreads foreach {
          data =>
            threadNameQuery += ThreadNameRow(threadId = data.id, hostId = hostId, name = data.name)
        }
    }
  }

  def deleteThreadInfo(hostId: String): Unit = {
    db withLockedSession {
      implicit session => 
      recordsForHostId(hostId).delete
    }
  }

  def threadNamesList(hostId: String): List[ThreadNameRow] = {
    db withLockedSession {
      implicit session =>
        recordsForHostId(hostId).list()
    }
  }


  def recordsForHostId(hostId: String): Query[ThreadNameTable, ThreadNameRow] = {
    for (record <- threadNameQuery if record.hostId === hostId) yield record
  }
}
