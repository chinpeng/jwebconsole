package org.jwebconsole.server.jmx

import org.specs2.mutable.{Before, SpecificationWithJUnit}
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import javax.management.remote.JMXConnector
import org.jwebconsole.server.context.host.HostData
import org.jwebconsole.server.readmodel.hostlist.SimpleHostView

class JMXConnectionSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {

  trait mocks extends Before {
    val parser = mock[JMXDataParser]
    val util = mock[JMXConnectionUtil]
    val connector = mock[JMXConnector]
    val hostName = "localhost"
    val port = 1234
    val host = SimpleHostView("tes-id", hostName, port)

    def before: Unit = {
      util.connect(anyString, any[Map[String, Any]]) returns connector
    }

  }

  "JMX Connection" should {
    "return disconnected Host Data" in new mocks {
      val connection = new JMXConnection(host, parser, util)
      parser.parse(any[JMXConnector]) throws new RuntimeException
      val model = connection.retrieveHostData
      model.connected must beFalse
    }
  }

  "JMX Connection" should {
    "close connector" in new mocks {
      val connection = new JMXConnection(host, parser, util)
      val model = connection.retrieveHostData
      there was one(connector).close()
    }
  }

  "JMX Connection" should {
    "return parsed data on success" in new mocks {
      val connection = new JMXConnection(host, parser, util)
      parser.parse(any[JMXConnector]) returns HostData(connected = true)
      val model = connection.retrieveHostData
      model.connected must beTrue
    }
  }

}
