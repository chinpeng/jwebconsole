package org.jwebconsole.server.jmx

import javax.management.remote.JMXConnector
import scala.util.{Failure, Success, Try}
import javax.management.ObjectName
import javax.management.openmbean.CompositeData

class JMXWrapper(private val connection: Try[JMXConnector]) {

  def heapUsage: Try[HeapMemoryUsage] = {
    for (conn <- connection;
         heapUsage <- getHeapUsage(conn)) yield heapUsage
  }

  private def getHeapUsage(connector: JMXConnector): Try[HeapMemoryUsage] = {
    try {
      val memoryObject = connector.getMBeanServerConnection.getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage")
      val memory = memoryObject.asInstanceOf[CompositeData]
      def getByKey(key: String): Long = {
        memory.get(key).asInstanceOf[Long]
      }
      makeResult(getByKey)
    } catch {
      case e: Exception => Failure(e)
    }

  }


  private def makeResult(getByKey: (String) => Long): Success[HeapMemoryUsage] = {
    Success(
      HeapMemoryUsage(
        committed = getByKey("committed"),
        init = getByKey("init"),
        max = getByKey("max"),
        used = getByKey("used")
      )
    )
  }
}

object JMXWrapper {

  def apply(host: HostInfo, receiver: JMXConnectionReceiver): JMXWrapper = {
    try {
      val c = receiver.newConnection(host)
      c.connect()
      new JMXWrapper(Success(c))
    } catch {
      case e: Exception =>
        new JMXWrapper(Failure(e))
    }

  }

}

