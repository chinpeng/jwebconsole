package org.jwebconsole.server.jmx

import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}

class JMXConnectionChecker {

  def checkConnection(host: String, port: Int): Unit = {
    val url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"
    val serviceUrl = new JMXServiceURL(url)
    val jmxConnector = JMXConnectorFactory.connect(serviceUrl, null)
    jmxConnector.getConnectionId
    jmxConnector.close()
  }

}
