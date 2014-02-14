package org.jwebconsole.server.jmx

import org.jwebconsole.server.context.host.{ThreadData, HostData}
import javax.management.remote.JMXConnector
import org.jwebconsole.server.jmx.converter.ThreadDataConverter

class JMXDataParser {

  val converters = List(
    new ThreadDataConverter()
  )

  def parse(connection: JMXConnector): HostData = {
    var hostData = HostData()
    converters.foreach {
      conv =>
        hostData = conv.fromConnection(connection, hostData)
    }
    hostData
  }


}

object JMXDataParser {

  def apply(): JMXDataParser = {
    new JMXDataParser()
  }

}
