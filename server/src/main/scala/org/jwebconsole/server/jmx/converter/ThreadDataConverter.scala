package org.jwebconsole.server.jmx.converter

import org.jwebconsole.server.context.host.{HostData, ThreadData}
import javax.management.remote.JMXConnector
import javax.management.ObjectName

class ThreadDataConverter extends JMXDataConverter {

  def fromConnection(connection: JMXConnector, hostData: HostData): HostData = {
    val withConnection = count(connection)(_)
    val threadCount = withConnection("ThreadCount")
    val peakThreadCount = withConnection("PeakThreadCount")
    val result = ThreadData(threadCount, peakThreadCount)
    hostData.copy(threadData = result)
  }

  def count(connection: JMXConnector)(attr: String): Int = {
    connection.getMBeanServerConnection.getAttribute(new ObjectName("java.lang:type=Threading"), attr).asInstanceOf[Int]
  }
}
