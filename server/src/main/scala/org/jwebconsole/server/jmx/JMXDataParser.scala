package org.jwebconsole.server.jmx

import org.jwebconsole.server.context.host.{ThreadData, HostData}
import javax.management.remote.JMXConnector
import org.jwebconsole.server.jmx.converter.ThreadDataConverter
import java.util.Date
import scala.util.Try
import org.slf4j.LoggerFactory

class JMXDataParser {

  private val log = LoggerFactory.getLogger(classOf[JMXConnection])

  val converters = List(
    new ThreadDataConverter()
  )

  def parse(connection: JMXConnector): HostData = {
    var hostData = HostData(connected = true)
    converters.foreach {
      conv =>
        val converted = Try(conv.fromConnection(connection, hostData))
        converted.map(hostData = _)
        converted.failed.map(err => log.error("Unable to parse data", err))
    }
    hostData
  }


}

object JMXDataParser {

  def apply(): JMXDataParser = {
    new JMXDataParser()
  }

}
