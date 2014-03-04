package org.jwebconsole.server.jmx

import javax.management.remote.{JMXConnectorFactory, JMXServiceURL, JMXConnector}
import java.lang.management.{OperatingSystemMXBean, ThreadMXBean, ManagementFactory}

class JMXConnectionUtil {

  def connect(url: String): JMXConnector = {
    val serviceUrl = new JMXServiceURL(url)
    JMXConnectorFactory.connect(serviceUrl, null)
  }

  def getThreadBean(connection: JMXConnector): ThreadMXBean = {
    ManagementFactory.newPlatformMXBeanProxy(connection.getMBeanServerConnection, ManagementFactory.THREAD_MXBEAN_NAME, classOf[ThreadMXBean])
  }

  def getOperatingSystemBean(connection: JMXConnector): OperatingSystemMXBean = {
    ManagementFactory.newPlatformMXBeanProxy(connection.getMBeanServerConnection, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, classOf[OperatingSystemMXBean])
  }

}
