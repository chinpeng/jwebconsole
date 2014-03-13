package org.jwebconsole.server.jmx

import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}
import org.jwebconsole.server.util.Converters._
import java.util

class JMXConnectionChecker {

  def checkConnection(host: String, port: Int, user: String, password: String): Unit = {
    val url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"
    val serviceUrl = new JMXServiceURL(url)
    val jmxConnector = JMXConnectorFactory.connect(serviceUrl, createCredentialsMap(user, password))
    jmxConnector.getConnectionId
    jmxConnector.close()
  }

  def createCredentialsMap(login: String, password: String): java.util.Map[String, _] = {
    var result: java.util.Map[String, Any] = null
    login.emptyToOption().foreach {
      login =>
        result = new util.HashMap[String, Any]()
        val credentials = Array(login, password)
        result.put("jmx.remote.credentials", credentials)
    }
    result
  }


}
