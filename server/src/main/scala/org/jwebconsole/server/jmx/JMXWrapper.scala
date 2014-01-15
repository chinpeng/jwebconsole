package org.jwebconsole.server.jmx

import javax.management.ObjectName
import javax.management.openmbean.CompositeData
import javax.management.remote.{JMXConnector, JMXServiceURL, JMXConnectorFactory}

class JMXWrapper(private val connection: JMXConnector) {

  def heapUsage: HeapMemoryUsage = {
    val memoryObject = connection.getMBeanServerConnection.getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage")
    val memory = memoryObject.asInstanceOf[CompositeData]
    def getByKey(key: String): Long = {
      memory.get(key).asInstanceOf[Long]
    }
    HeapMemoryUsage(
      committed = getByKey("committed"),
      init = getByKey("init"),
      max = getByKey("max"),
      used = getByKey("used")
    )
  }

}

object JMXWrapper {

  def apply(host: String, port: Int): JMXWrapper = {
    val c = JMXConnectorFactory.newJMXConnector(createConnectionURL(host, port), null)
    c.connect()
    new JMXWrapper(c)
  }

  def createConnectionURL(host: String, port: Int): JMXServiceURL = {
    new JMXServiceURL("rmi", "", 0, "/jndi/rmi://" + host + ":" + port + "/jmxrmi")
  }

}

case class HeapMemoryUsage(committed: Long = 0, init: Long = 0, max: Long = 0, used: Long = 0)
