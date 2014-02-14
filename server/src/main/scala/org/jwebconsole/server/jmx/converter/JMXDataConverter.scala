package org.jwebconsole.server.jmx.converter

import javax.management.remote.JMXConnector
import org.jwebconsole.server.context.host.HostData

trait JMXDataConverter {

  def fromConnection(connection: JMXConnector, hostData: HostData): HostData

}
