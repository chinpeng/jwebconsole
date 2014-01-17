package org.jwebconsole.server.jmx

import javax.management.remote.JMXConnector
import scala.util.{Failure, Success, Try}
import javax.management.ObjectName
import javax.management.openmbean.CompositeData
import org.jwebconsole.server.model.{HostInfo, JMXHostStatus}

class JMXWrapper(private var connector: JMXConnector) {

  def connection: Try[JMXConnector] = {
    Try(connector.getConnectionId) match {
      case Success(v) =>
        Success(connector)
      case Failure(e) =>
        Try {
          connector.connect()
          connector
        }
    }
  }

  def heapUsage: Try[HeapMemoryUsage] = {
    for (conn <- connection;
         heapUsage <- getHeapUsage(conn)) yield heapUsage
  }

  def status: JMXHostStatus = {
    connection match {
      case Failure(e) => JMXHostStatus(available = false, connected = true)
      case Success(v) => JMXHostStatus(available = true, connected = true)
    }
  }

  private def getHeapUsage(connector: JMXConnector): Try[HeapMemoryUsage] = Try {
    val memoryObject = connector.getMBeanServerConnection.getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage")
    val memory = memoryObject.asInstanceOf[CompositeData]
    def getByKey(key: String): Long = {
      memory.get(key).asInstanceOf[Long]
    }
    makeResult(getByKey)
  }

  private def makeResult(getByKey: (String) => Long): HeapMemoryUsage = {
    HeapMemoryUsage(
      committed = getByKey("committed"),
      init = getByKey("init"),
      max = getByKey("max"),
      used = getByKey("used")
    )
  }
}

object JMXWrapper {

  def apply(host: HostInfo, receiver: JMXConnectionReceiver): JMXWrapper = {
    val c = receiver.newConnection(host)
    new JMXWrapper(c)
  }

}

case class HeapMemoryUsage(committed: Long = 0, init: Long = 0, max: Long = 0, used: Long = 0)

