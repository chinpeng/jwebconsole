package org.jwebconsole.server.jmx

import javax.management.remote.{JMXConnectorFactory, JMXServiceURL}
import scala.collection.JavaConverters._
import org.jwebconsole.server.util.Converters._

class JMXConnectionChecker {

  def checkConnection(host: String, port: Int, user: String, password: String): Unit = {
    val url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"
    val serviceUrl = new JMXServiceURL(url)
    val jmxConnector = JMXConnectorFactory.newJMXConnector(serviceUrl, createCredentialsMap(user, password).asJava)
    jmxConnector.getConnectionId
    jmxConnector.close()
  }

  def createCredentialsMap(login: String, password: String): Map[String, _] = {
    var result = Map.empty[String, Any]
    login.emptyToOption().foreach {
      login =>
        val credentials = Array(login, password)
        result += ("jmx.remote.credentials" -> credentials)
    }
    result
  }


}
