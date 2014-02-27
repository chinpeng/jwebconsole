package org.jwebconsole.server.readmodel.common

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import akka.actor._
import org.jwebconsole.server.context.common.{ReplayFinished, AppEvent}
import org.specs2.mock.Mockito
import akka.testkit.TestProbe
import scala.concurrent.duration._

class ReadModelReplayingActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val defaultFilterFunc: PartialFunction[AppEvent, Boolean] = {
      case _ => true
    }
    val dao = mock[ReplayingDAO]
    val probe = TestProbe()
    val probeRef = probe.ref

  }

  "Replaying actor" should {
    "UnBecome after finish" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      probe.expectMsg("Done")
      actor ! "Test"
      probe.expectMsg("Test")
    }
  }

  "Replaying actor" should {
    "send 'after recover' message" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      probe.expectMsg("Done")
    }
  }

  "Replaying actor" should {
    "Unbecome on DB failure" in new mocks {
      dao.exists() throws new RuntimeException()
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      actor ! "Test"
      probe.expectMsg("Test")
    }
  }

  "Replaying actor" should {
    "not create table if table exists" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      there was no(dao).createTable()
    }
  }

  "Replaying actor" should {
    "not create table if table exists" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      there was no(dao).createTable()
    }
  }

  "Replaying actor" should {
    "UnBecome after finishing event" in new mocks {
      dao.exists() returns false
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      actor ! ReplayFinished
      probe.expectMsg("Done")
      actor ! "test"
      probe.expectMsg("test")
    }
  }

  "Replaying actor" should {
    "stash events while replaying" in new mocks {
      dao.exists() returns false
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      actor ! "test"
      probe.expectNoMsg(100.millis)
      actor ! ReplayFinished
      probe.expectMsg("Done")
      probe.expectMsg("test")
    }
  }

  "Replaying actor" should {
    "persist events in replaying state" in new mocks {
      dao.exists() returns false
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, defaultFilterFunc, dao)))
      actor ! "test"
      probe.expectNoMsg(100.millis)
      actor ! ReplayFinished
      probe.expectMsg("Done")
      probe.expectMsg("test")
    }
  }



  class ReplayingActor(req: ActorRef, val filterFunc: PartialFunction[AppEvent, Boolean], val dao: ReplayingDAO) extends Actor with Stash with ActorLogging with ReadModelReplayingActor {

    override def receive: Receive = {
      case msg => req ! msg
    }

    override def persistReplay(ev: AppEvent): Unit = {
      req ! "Persisted"
    }

    override def afterRecover(): Unit = {
      req ! "Done"
    }
  }

}
