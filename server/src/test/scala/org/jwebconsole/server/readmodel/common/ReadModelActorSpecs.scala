package org.jwebconsole.server.readmodel.common

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{ErrorMessages, AkkaTestkitSupport}
import akka.actor._
import org.jwebconsole.server.context.common.{ResponseMessage, ReplayFinished, AppEvent}
import org.specs2.mock.Mockito
import akka.testkit.TestProbe
import scala.concurrent.duration._
import org.jwebconsole.server.context.host.HostDeletedEvent

class ReadModelActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[CustomDao]
    val probe = TestProbe()
    val probeRef = probe.ref
    val req = SimpleRequest(10)
    val expectedResponse = ResponseMessage(body = Some(10))
    val ev = HostDeletedEvent("test-id")
  }

  "Replaying actor" should {
    "UnBecome after finish" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      probe.expectMsg("Done")
      actor ! req
      expectMsg(expectedResponse)
    }
  }

  "Replaying actor" should {
    "send 'after recover' message" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      probe.expectMsg("Done")
    }
  }

  "Replaying actor" should {
    "Unbecome on DB failure" in new mocks {
      dao.exists() throws new RuntimeException()
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      actor ! req
      expectMsg(expectedResponse)
    }
  }

  "Replaying actor" should {
    "not create table if table exists" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      there was no(dao).createTable()
    }
  }

  "Replaying actor" should {
    "UnBecome after finishing event" in new mocks {
      dao.exists() returns false
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      probe.expectMsg("Done")
      actor ! req
      expectMsg(expectedResponse)
    }
  }

  "Replaying actor" should {
    "stash events while replaying" in new mocks {
      dao.exists() returns false
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      actor ! req
      probe.expectNoMsg(100.millis)
      actor ! ReplayFinished
      probe.expectMsg("Done")
      expectMsg(expectedResponse)
    }
  }

  "Replaying actor" should {
    "persist events in replaying state" in new mocks {
      dao.exists() returns false
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      actor ! req
      probe.expectNoMsg(100.millis)
      actor ! ReplayFinished
      probe.expectMsg("Done")
      expectMsg(expectedResponse)
    }
  }

  "Replaying actor" should {
    "continue work on db persist failure" in new mocks {
      dao.exists() returns false
      dao.throwException() throws new RuntimeException()
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      actor ! ev
      probe.expectMsg("Done")
      actor ! req
      expectMsg(expectedResponse)
    }
  }

  "Replaying actor" should {
    "respond with error msg on DB failure" in new mocks {
      dao.exists() returns true
      val actor = system.actorOf(Props(new ReplayingActor(probeRef, dao)))
      probe.expectMsg("Done")
      actor ! FailureRequest()
      expectMsg(ResponseMessage(error = Some(ErrorMessages.UnknownErrorMessage)))
    }
  }


  class ReplayingActor(req: ActorRef, val dao: CustomDao) extends ReadModelActor {

    override def processRequest: PartialFunction[ReadModelRequest, Any] = {
      case SimpleRequest(id) => id
      case req: FailureRequest => throw new RuntimeException
    }

    override def persistEvent: PartialFunction[AppEvent, Unit] = {
      case ev: HostDeletedEvent => dao.throwException()
      case _ => req ! "Persisted"
    }

    override def afterRecover(): Unit = {
      req ! "Done"
    }

  }

  trait CustomDao extends ReplayingDao {
    def throwException()
  }


  case class SimpleRequest(id: Int) extends ReadModelRequest

  case class FailureRequest() extends ReadModelRequest

}
