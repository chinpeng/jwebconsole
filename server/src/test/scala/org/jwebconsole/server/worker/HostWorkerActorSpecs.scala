package org.jwebconsole.server.worker

import org.specs2.mutable.{SpecificationWithJUnit, Specification}
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import org.jwebconsole.server.jmx.{JMXConnection, JMXConnectionFactory}
import akka.testkit.{TestActorRef, TestProbe}
import akka.actor.{Terminated, Props}
import scala.util.Success
import org.jwebconsole.server.context.host.{HostData, ChangeHostDataCommand, HostParametersChangedEvent}
import org.jwebconsole.server.readmodel.hostlist.SimpleHostView

class HostWorkerActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {

  sequential

  trait mocks extends AkkaTestkitSupport {
    val connectionFactory = mock[JMXConnectionFactory]
    val connection = mock[JMXConnection]
    connectionFactory.createConnection(anyString, anyInt) returns connection
    val commandHandler = TestProbe()
    val handlerRef = commandHandler.ref
    val probe = TestProbe()
    val probeRef = probe.ref
    val host = SimpleHostView("test-id", "localhost", 8080)
    val worker: TestActorRef[HostWorkerActor] = TestActorRef(Props(new HostWorkerActor(host, handlerRef, connectionFactory)))
    val workerSource = worker.underlyingActor

    def expectTerminated(): Unit = {
      probe.expectMsgPF() {
        case terminated: Terminated => true
      }
    }

  }

  "Host worker" should {
    "stop self on receiving stop message" in new mocks {
      probe.watch(worker)
      worker ! StopWork()
      expectTerminated()
    }
  }

  "Host worker" should {
    "stop timer on stop message" in new mocks {
      probe.watch(worker)
      worker ! StartWork()
      worker ! StopWork()
      workerSource.timer.map {
        _.isCancelled must beTrue
      }
    }
  }

  "Host worker" should {
    "start timer on start message" in new mocks {
      probe.watch(worker)
      worker ! StartWork()
      workerSource.timer.map {
        _.isCancelled must beFalse
      }
    }
  }

  "Host worker" should {
    "start timer on start message" in new mocks {
      probe.watch(worker)
      worker ! StartWork()
      workerSource.timer.map {
        _.isCancelled must beFalse
      }
    }
  }

  "Host worker" should {
    "create new connection on change event" in new mocks {
      val anotherConnection = mock[JMXConnection]
      connectionFactory.createConnection(anyString, anyInt) returns anotherConnection
      worker ! StartWork()
      worker ! HostParametersChangedEvent("test-id", "localhost", 8080)
      worker ! MakeConnectionPolling
      workerSource.connection match {
        case conn if conn == anotherConnection => success
        case conn if conn == connection => failure
      }
    }
  }

  "Host worker" should {
    "change host params on change event" in new mocks {
      val anotherConnection = mock[JMXConnection]
      worker ! StartWork()
      worker ! HostParametersChangedEvent("test-id", "changedHost", 8080)
      worker ! MakeConnectionPolling
      workerSource.host.name mustEqual "changedHost"
    }
  }

  "Host worker" should {
    "send to host command actor 'connected' message" in new mocks {
      connection.retrieveHostData returns HostData(connected = true)
      worker ! StartWork()
      worker ! MakeConnectionPolling
      commandHandler.expectMsgPF() {
        case ChangeHostDataCommand("test-id", data) if data.connected => true
      }
    }
  }

  "Host worker" should {
    "send to host command actor 'not connected' message" in new mocks {
      connection.retrieveHostData returns HostData(connected = false)
      worker ! StartWork()
      worker ! MakeConnectionPolling
      commandHandler.expectMsgPF() {
        case ChangeHostDataCommand("test-id", data) if !data.connected => true
      }
    }
  }

}
