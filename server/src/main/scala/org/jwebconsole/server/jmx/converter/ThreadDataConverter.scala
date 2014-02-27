package org.jwebconsole.server.jmx.converter

import org.jwebconsole.server.context.host.{AvailableThread, HostData, ThreadData}
import javax.management.remote.JMXConnector
import javax.management.ObjectName

class ThreadDataConverter extends JMXDataConverter {

  val threadingObjectName = new ObjectName("java.lang:type=Threading")

  def fromConnection(connection: JMXConnector, hostData: HostData): HostData = {
    val attr = attributeGetter(connection)(_)
    val threadCount: Int = attr("ThreadCount")
    val peakThreadCount: Int = attr("PeakThreadCount")
    val threads = availableThreads(attr)
    val result = ThreadData(threadCount, peakThreadCount, threads)
    hostData.copy(threadData = result)
  }

  def count(connection: JMXConnector)(attr: String): Int = {
    connection.getMBeanServerConnection.getAttribute(threadingObjectName, attr).asInstanceOf[Int]
  }

  def availableThreads(attr: (String) => Any): List[AvailableThread] = {
    val threadIds = attr("AllThreadIds").asInstanceOf[Array[Long]]
    for (threadId <- threadIds;
          )
  }

  def attributeGetter[T](connection: JMXConnector)(attr: String): T = {
    connection.getMBeanServerConnection.getAttribute(threadingObjectName, attr).asInstanceOf[T]
  }
}
