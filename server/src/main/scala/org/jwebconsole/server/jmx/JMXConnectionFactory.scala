package org.jwebconsole.server.jmx

import scala.util.Try
import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}


class JMXConnectionFactory {

  def createConnection(hostName: String, port: Int): Try[JMXConnection] = Try {
    val url = "service:jmx:rmi:///jndi/rmi://" + hostName + ":" + port + "/jmxrmi"
    val serviceUrl = new JMXServiceURL(url)
    val connector = JMXConnectorFactory.connect(serviceUrl, null)
    new JMXConnection(connector)
  }

}
