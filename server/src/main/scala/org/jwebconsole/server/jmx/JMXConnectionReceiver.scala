package org.jwebconsole.server.jmx

import javax.management.remote.{JMXConnectorFactory, JMXConnector, JMXServiceURL}
import scala.collection.JavaConversions._
import org.jwebconsole.server.model.HostInfo

class JMXConnectionReceiver {

  def newConnection(host: HostInfo, params: java.util.Map[String, AnyRef] = Map.empty[String, AnyRef]): JMXConnector = {
    JMXConnectorFactory.newJMXConnector(createConnectionURL(host.name, host.port), params)
  }

  def createConnectionURL(host: String, port: Int): JMXServiceURL = {
    new JMXServiceURL("rmi", "", 0, "/jndi/rmi://" + host + ":" + port + "/jmxrmi")
  }

}
