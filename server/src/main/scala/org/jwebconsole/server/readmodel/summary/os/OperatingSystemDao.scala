package org.jwebconsole.server.readmodel.summary.os

import org.jwebconsole.server.readmodel.common.ReplayingDao
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable
import org.jwebconsole.server.context.host.OperatingSystemData

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 11:50
 */
class OperatingSystemDao(private val db: Database) extends ReplayingDao {

  val operatingSystemTableName = "operating_system_table"

  case class OperatingSystemRow(hostId: String,
                                architecture: String,
                                availableProcessors: Int,
                                systemLoadAverage: Double,
                                name: String,
                                version: String,
                                processCPUTime: Long,
                                committedVirtualMemorySize: Long,
                                totalPhysicalMemorySize: Long,
                                freePhysicalMemorySize: Long,
                                totalSwapSpaceSize: Long,
                                freeSwapSpaceSize: Long)

  case class OperatingSystemTable(tag: Tag) extends Table[OperatingSystemRow](tag, operatingSystemTableName) {

    def hostId = column[String]("hostId", O.PrimaryKey)
    def architecture = column[String]("architecture")
    def availableProcessors = column[Int]("availableProcessors")
    def systemLoadAverage = column[Double]("systemLoadAverage")
    def name = column[String]("name")
    def version = column[String]("version")
    def processCpuTime = column[Long]("processCpuTime")
    def committedVirtualMemorySize = column[Long]("committedVirtualMemorySize")
    def totalPhysicalMemorySize = column[Long]("totalPhysicalMemorySize")
    def freePhysicalMemorySize = column[Long]("freePhysicalMemorySize")
    def totalSwapSpaceSize = column[Long]("totalSwapSpaceSize")
    def freeSwapSpaceSize = column[Long]("freeSwapSpaceSize")

    def * = (hostId, architecture, availableProcessors, systemLoadAverage,name, version, processCpuTime,
             committedVirtualMemorySize, totalPhysicalMemorySize, freePhysicalMemorySize, totalSwapSpaceSize, freeSwapSpaceSize) <>
      (OperatingSystemRow.tupled, OperatingSystemRow.unapply)

  }

  val operatingSystemQuery = TableQuery[OperatingSystemTable]

  def createTable(): Unit = {
    db withLockedSession {
      implicit session =>
        operatingSystemQuery.ddl.create
    }
  }

  override def exists(): Boolean = {
    db withLockedSession {
      implicit session =>
        !MTable.getTables(operatingSystemTableName).list().isEmpty
    }
  }

  def deleteOperatingSystemInfo(hostId: String): Unit = {
    db withLockedSession {
      implicit session => recordsForHost(hostId).delete
    }
  }

  def refreshOperatingSystemInfo(hostId: String, data: OperatingSystemData): Unit = {
    db withLockedSession {
      implicit session =>
        deleteOperatingSystemInfo(hostId)
        operatingSystemQuery += OperatingSystemRow(hostId = hostId,
                                                   architecture = data.architecture,
                                                   systemLoadAverage = data.systemLoadAverage,
                                                   availableProcessors = data.availableProcessors,
                                                   name = data.name,
                                                   version = data.version,
                                                   processCPUTime = data.processCPUTime,
                                                   committedVirtualMemorySize = data.committedVirtualMemorySize,
                                                   totalPhysicalMemorySize = data.totalPhysicalMemorySize,
                                                   freePhysicalMemorySize = data.freePhysicalMemorySize,
                                                   totalSwapSpaceSize = data.totalSwapSpaceSize,
                                                   freeSwapSpaceSize = data.freeSwapSpaceSize)
    }
  }

  def getOperatingSystemInfo(hostId: String): OperatingSystemRow = {
    db withLockedSession {
      implicit session => recordsForHost(hostId).list().headOption.get
    }
  }

  private def recordsForHost(hostId: String): Query[OperatingSystemTable, OperatingSystemRow] = {
    for (record <- operatingSystemQuery; if record.hostId === hostId) yield record
  }
}
