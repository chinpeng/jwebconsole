package org.jwebconsole.server.worker

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import org.jwebconsole.server.jmx.JMXConnectionFactory
import akka.testkit.{TestActorRef, TestProbe}
import org.jwebconsole.server.context.host.model.SimpleHostView
import akka.actor.{Terminated, Props}

class HostWorkerActorSpecs extends Specification with Mockito with NoTimeConversions {

  sequential

  trait mocks extends AkkaTestkitSupport {
    val connectionFactory = mock[JMXConnectionFactory]
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
    "start polling on receiving start message" in new mocks {

    }
  }

}
