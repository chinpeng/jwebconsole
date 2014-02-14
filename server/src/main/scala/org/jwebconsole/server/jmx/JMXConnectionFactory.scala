package org.jwebconsole.server.jmx


class JMXConnectionFactory {

  def createConnection(hostName: String, port: Int): JMXConnection = new JMXConnection(hostName, port, JMXDataParser())

}
