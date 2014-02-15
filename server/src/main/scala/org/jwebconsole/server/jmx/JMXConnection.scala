package org.jwebconsole.server.jmx

import javax.management.remote.{JMXServiceURL, JMXConnectorFactory, JMXConnector}
import scala.util.{Success, Failure, Try}
import org.slf4j.LoggerFactory
import org.jwebconsole.server.context.host.HostData


class JMXConnection(private val host: String, private val port: Int, parser: JMXDataParser) {

  val log = LoggerFactory.getLogger(classOf[JMXConnection])

  def retrieveHostData: HostData = {
    withConnection {
      connection =>
        parser.parse(connection)
    } match {
      case Success(v) => v
      case Failure(e) => HostData()
    }
  }

  private def withConnection[T](action: JMXConnector => T): Try[T] = {
    val res = Try {
      val url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi"
      val serviceUrl = new JMXServiceURL(url)
      val connector = JMXConnectorFactory.connect(serviceUrl, null)
      val result = action(connector)
      connector.close()
      result
    }
    if (res.isFailure)
      log.debug("Unable to connect to host: $host:$port")
    res
  }
}