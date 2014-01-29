package org.jwebconsole.server.context.common

import org.specs2.mutable.{Before, Specification}
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import akka.testkit.{TestActorRef, TestActor, TestProbe}
import akka.actor.Props
import org.jwebconsole.server.context.host.{HostCreatedEvent, HostDeletedEvent}
import scala.concurrent.duration._

class EventStoreReplayingActorSpecs extends Specification with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val probe = TestProbe()
    val probeRef = probe.ref
    val store = system.actorOf(Props[GlobalEventStore])
    val id = "test-id"

    def grabAllFunction: PartialFunction[AppEvent, Boolean] = {
      case _ => true
    }

  }

  "replaying actor" should {
    "send replayed events back to receiver" in new mocks {
      store ! HostDeletedEvent(id)
      Thread.sleep(500)
      val actor = system.actorOf(Props(new EventStoreReplayingActor(grabAllFunction, probeRef)))
      probe.expectMsg(HostDeletedEvent(id))
    }
  }

  "replaying actor" should {
    "not send not matching replayed events back to receiver" in new mocks {
      def func: PartialFunction[AppEvent, Boolean] = {
        case ev: HostCreatedEvent => true
        case _ => false
      }

      store ! HostDeletedEvent(id)
      Thread.sleep(500)
      val actor = system.actorOf(Props(new EventStoreReplayingActor(func, probeRef, 10.millis)))
      probe.expectMsg(ReplayFinished)
    }
  }

  "replaying actor" should {
    "should cancel timer after finish" in new mocks {
      val actor:TestActorRef[EventStoreReplayingActor] = TestActorRef(Props(new EventStoreReplayingActor(grabAllFunction, probeRef, 10.millis)))
      val source = actor.underlyingActor
      probe.expectMsg(ReplayFinished)
      source.cancel.isCancelled mustEqual true
    }
  }

}
