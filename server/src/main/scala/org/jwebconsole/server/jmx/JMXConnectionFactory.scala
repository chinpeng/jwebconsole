package org.jwebconsole.server.jmx

import org.jwebconsole.server.readmodel.hostlist.SimpleHostView


class JMXConnectionFactory {

  def createConnection(host: SimpleHostView): JMXConnection = new JMXConnection(host, JMXDataParser())

}
