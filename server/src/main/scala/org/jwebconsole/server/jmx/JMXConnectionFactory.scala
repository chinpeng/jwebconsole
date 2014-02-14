package org.jwebconsole.server.jmx


class JMXConnectionFactory {

  def createConnection(hostName: String, port: Int): JMXConnection = new JMXConnection(hostName, port)

  /*val url = "service:jmx:rmi:///jndi/rmi://" + hostName + ":" + port + "/jmxrmi"
  val serviceUrl = new JMXServiceURL(url)
  val connector = JMXConnectorFactory.connect(serviceUrl, null)
  new JMXConnection(connector)*/


}
