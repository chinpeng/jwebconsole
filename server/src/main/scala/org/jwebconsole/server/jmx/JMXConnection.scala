package org.jwebconsole.server.jmx

import javax.management.remote.JMXConnector
import scala.util.{Success, Failure, Try}
import org.slf4j.LoggerFactory
import org.jwebconsole.server.context.host.HostData
import org.jwebconsole.server.readmodel.hostlist.SimpleHostView
import org.jwebconsole.server.util.Converters._
import java.util

class JMXConnection(private val host: SimpleHostView,
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
    val url = "service:jmx:rmi:///jndi/rmi://" + host.name + ":" + host.port + "/jmxrmi"
    val connector = util.connect(url, createCredentialsMap())
    val result = action(connector)
    connector.close()
    result
  }

  private def createCredentialsMap(): java.util.Map[String, Any] = {
      var result: java.util.Map[String, Any] = null
      host.login.emptyToOption().foreach {
        login =>
          result = new java.util.HashMap[String, Any]()
          val credentials = Array(login, host.password)
          result.put("jmx.remote.credentials", credentials)
      }
    result
  }


}