package org.jwebconsole.server.readmodel.summary.os

import org.jwebconsole.server.readmodel.common.ReplayingDao
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import org.jwebconsole.server.context.host.OperationSystemData

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 11:50
 */
class OperationSystemDao(private val db: Database) extends ReplayingDao {

  val operationSystemTableName = "operating_system_table"

  case class OperationSystemRow(id: Int = -1, architecture: String, availableProcessors: Int,
                                systemLoadAverage: Double, name: String, version: String)

  case class OperationSystemTable(tag: Tag) extends Table[OperationSystemRow](tag, operationSystemTableName) {

    def hostId = column[Int]("hostId", O.PrimaryKey, O.AutoInc)

    def architecture = column[String]("architecture")

    def availableProcessors = column[Int]("availableProcessors")

    def systemLoadAverage = column[Double]("systemLoadAverage")

    def name = column[String]("name")

    def version = column[String]("version")

    def * = (hostId, architecture, availableProcessors, systemLoadAverage, name, version) <> (OperationSystemRow.tupled, OperationSystemRow.unapply)

  }

  val operationSystemQuery = TableQuery[OperationSystemTable]

  def createTable(): Unit = {
    db withLockedSession {
      implicit session =>
        operationSystemQuery.ddl.create
    }
  }

  override def exists(): Boolean = {
    db withLockedSession {
      implicit session =>
        !MTable.getTables(operationSystemTableName).list().isEmpty
    }
  }

  def deleteOperationSystemInfo(hostId: String): Unit = {
    db withLockedSession {
      implicit session => recordsForHost(hostId).delete
    }
  }

  def refreshOperrationSystemInfo(hostId: String, data: OperationSystemData): Unit = {
    db withLockedSession {
      implicit session =>
        deleteOperationSystemInfo(hostId)
        operationSystemQuery += OperationSystemRow(hostId = hostId,
                                                   architecture = data.architecture,
                                                   systemLoadAverage = data.systemLoadAverage,
                                                   availableProcessors = data.availableProcessors,
                                                   name = data.name,
                                                   version = data.version)
    }
  }

  def getOperationSystemInfo(hostId: String): OperationSystemRow = {
    db withLockedSession {
      implicit session => recordsForHost(hostId).list().headOption.get
    }
  }

  private def recordsForHost(hostId: String): Query[OperationSystemTable, OperationSystemRow] = {
    for (record <- operationSystemQuery; if record.hostId === hostId) yield record
  }
}
