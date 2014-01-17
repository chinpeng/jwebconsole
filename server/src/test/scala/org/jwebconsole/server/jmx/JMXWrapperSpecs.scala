package org.jwebconsole.server.jmx

import org.specs2.mutable.{Before, Specification}
import org.specs2.mock.Mockito
import javax.management.remote.JMXConnector
import scala.util.{Success, Failure}
import javax.management.{ObjectName, MBeanServerConnection}
import javax.management.openmbean.CompositeData
import java.io.IOException
import org.jwebconsole.server.model.HostInfo


class JMXWrapperSpecs extends Specification with Mockito {

  trait mocks extends Before {
    lazy val receiver = mock[JMXConnectionReceiver]
    lazy val connector = mock[JMXConnector]
    lazy val serverConnection = mock[MBeanServerConnection]
    lazy val data: CompositeData = mock[CompositeData]
    lazy val info = HostInfo("localhost", 8080)

    def before: Unit = {
      connector.getMBeanServerConnection returns serverConnection
      serverConnection.getAttribute(any[ObjectName], anyString) returns data
      receiver.newConnection(info) returns connector
    }
  }

  "JMX Wrapper" should {
    "return fail monad when connection is unsuccessful" in new mocks {
      connector.getConnectionId throws new IOException()
      connector.connect() throws new RuntimeException
      val wrapper = JMXWrapper(info, receiver)
      wrapper.heapUsage match {
        case Failure(e: RuntimeException) => success
        case Success(v) => failure
      }
    }
  }

  "JMX Wrapper" should {
    "return success monad when connection succeed" in new mocks {
      data.get(anyString) returns 0L.asInstanceOf[AnyRef]
      val wrapper = JMXWrapper(info, receiver)
      wrapper.heapUsage match {
        case Failure(e) => failure
        case _ => success
      }
    }
  }

  "JMX Wrapper" should {
    "return correct Memory Usage Info" in new mocks {
      data.get(anyString) returns 10L.asInstanceOf[AnyRef]
      val wrapper = JMXWrapper(info, receiver)
      wrapper.heapUsage match {
        case Success(usage) =>
          usage.init mustEqual 10L
          usage.max mustEqual 10L
          usage.committed mustEqual 10L
          usage.used mustEqual 10L
        case _ => failure
      }
    }
  }

  "JMX Wrapper" should {
    "return not available status when connection is unavailable" in new mocks {
      connector.getConnectionId throws new IOException()
      connector.connect() throws new RuntimeException
      val wrapper = JMXWrapper(info, receiver)
      wrapper.status.available must beFalse
    }
  }

}
