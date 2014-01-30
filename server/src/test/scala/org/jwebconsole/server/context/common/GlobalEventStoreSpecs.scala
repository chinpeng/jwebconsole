package org.jwebconsole.server.context.common

import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{AppConstants, AkkaTestkitSupport}
import akka.actor.Props
import akka.persistence.EventsourcedProcessor
import akka.testkit.TestProbe
import org.jwebconsole.server.context.host.HostDeletedEvent

class GlobalEventStoreSpecs extends Specification with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val store = system.actorOf(Props[GlobalEventStore])
    val probe = TestProbe()
    val probeRef = probe.ref
    val id ="test-id"
  }

  "global event store" should {
    "persist app events" in new mocks {
      store ! HostDeletedEvent(id)
      waitBeforePersist
      val replayActor = system.actorOf(Props[TestEventStoreReplayingActor])
      replayActor ! "ping"
      expectMsgPF() {
        case x :: xs => true
      }
    }
  }

  "global event store" should {
    "not persist unknown events" in new mocks {
      store ! "ping"
      waitBeforePersist
      val replayActor = system.actorOf(Props[TestEventStoreReplayingActor])
      replayActor ! "ping"
      expectMsgPF() {
        case Nil => true
      }
    }
  }


  def waitBeforePersist {
    Thread.sleep(100)
  }
}

class TestEventStoreReplayingActor extends EventsourcedProcessor {

  override val processorId = AppConstants.GlobalEventStoreProcessorId
  var events = List.empty[AppEvent]

  def receiveRecover: Receive = {
    case ev: AppEvent => events ::= ev
  }

  def receiveCommand: Receive = {
    case cmd => sender ! events
  }
}