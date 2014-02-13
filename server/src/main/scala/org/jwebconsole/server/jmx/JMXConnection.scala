package org.jwebconsole.server.jmx

import javax.management.remote.JMXConnector
import scala.util.{Failure, Success, Try}

class JMXConnection(private val connector: JMXConnector) {


  def disconnect(): Unit = {
    Try(connector.close())
  }

  def connected: Boolean = {
    Try(connector.getConnectionId) match {
      case Success(_) => true
      case Failure(e) => withRecover()
    }
  }

  def withRecover(): Boolean = {
    Try(connector.connect()) match {
      case Success(_) => true
      case Failure(_) => false
    }
  }

}