package org.jwebconsole.server.jmx

import javax.management.remote.JMXConnector
import scala.util.{Success, Failure, Try}
import org.slf4j.LoggerFactory
import org.jwebconsole.server.context.host.HostData


class JMXConnection(private val host: String,
                    private val port: Int,
                    private val parser: JMXDataParser,
                    private val util: JMXConnectionUtil = new JMXConnectionUtil()) {

  private val log = LoggerFactory.getLogger(classOf[JMXConnection])

  def retrieveHostData: HostData = {
    withConnection {
      connection =>
        parser.parse(connection)
    } match {
      case Success(v) => v
      case Failure(e) =>
        log.error("Unable to get data", e)
        HostData()
    }
  }

  private def withConnection[T](action: JMXConnector => T): Try[T] = Try {
    val url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"
    val connector = util.connect(url)
    val result = action(connector)
    connector.close()
    result
  }

}