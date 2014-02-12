package org.jwebconsole.server.worker

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import akka.testkit.{TestProbe, TestActorRef}
import akka.actor.{PoisonPill, ActorRef, Props}
import org.jwebconsole.server.context.host.model.{SimpleHostView, AvailableHostsList}
import org.jwebconsole.server.context.host.{HostDeletedEvent, HostCreatedEvent, HostParametersChangedEvent}

class HostWorkerProducerActorSpecs extends Specification with Mockito with NoTimeConversions {

  sequential

  trait mocks extends AkkaTestkitSupport {
    val handler = mock[ActorRef]
    val ref: TestActorRef[HostWorkerProducerActor] = TestActorRef(Props(new HostWorkerProducerActor(handler)))
    val actor = ref.underlyingActor
    val host = SimpleHostView("test-id", "localhost", 8080)
    val probe = TestProbe()
    val probeRef = probe.ref
  }

  "Host worker producer" should {
    "handle all available hosts message" in new mocks {
      ref ! AvailableHostsList(List(host))
      actor.workers.get("test-id") match {
        case Some(v) => success
        case None => failure
      }
    }
  }

  "Host worker producer" should {
    "store all received hosts" in new mocks {
      ref ! AvailableHostsList(List(host, host.copy(id = "test-id2")))
      actor.workers.size mustEqual 2
    }
  }

  "Host worker producer" should {
    "handle all received hosts" in new mocks {
      ref ! AvailableHostsList(List(host, host.copy(id = "test-id2")))
      actor.workers.size mustEqual 2
    }
  }

  "Host worker producer" should {
    "reject host parameters changed event if no host exists" in new mocks {
      ref ! HostParametersChangedEvent(id = "test-id", name = "test-name", port = 8080)
      actor.workers.size mustEqual 0
    }
  }

  "Host worker producer" should {
    "should fire change event to Worker" in new mocks {
      actor.workers += ("test-id" -> probeRef)
      val ev = HostParametersChangedEvent(id = "test-id", name = "test-name", port = 8080)
      ref ! ev
      probe.expectMsg(ev)
    }
  }

  "Host worker producer" should {
    "add worker on host created event" in new mocks {
      ref ! HostCreatedEvent(id = "test-id", name = "test-name", port = 8080)
      actor.workers.size mustEqual 1
    }
  }

  "Host worker producer" should {
    "stop worker if it was already created" in new mocks {
      actor.workers += ("test-id" -> probeRef)
      ref ! HostCreatedEvent(id = "test-id", name = "test-name", port = 8080)
      probe.expectMsg(StopWork())
    }
  }

  "Host worker producer" should {
    "stop worker if received deletion event" in new mocks {
      actor.workers += ("test-id" -> probeRef)
      ref ! HostDeletedEvent(id = "test-id")
      probe.expectMsg(StopWork())
    }
  }

  "Host worker producer" should {
    "remove worker if received deletion event" in new mocks {
      actor.workers += ("test-id" -> probeRef)
      ref ! HostDeletedEvent(id = "test-id")
      actor.workers.size mustEqual 0
    }
  }

  "Host worker producer" should {
    "should stop all previous workers on Host List Event" in new mocks {
      actor.workers += ("test-id" -> probeRef)
      ref ! AvailableHostsList(List(host))
      probe.expectMsg(StopWork())
    }
  }

}
