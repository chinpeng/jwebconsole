package org.jwebconsole.server.readmodel.threads.count


import org.jwebconsole.server.util.{ErrorMessages, AkkaTestkitSupport}
import akka.testkit.{TestActorRef, TestProbe}
import akka.actor.Props
import org.jwebconsole.server.context.host._
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.context.host.HostDataChangedEvent
import org.jwebconsole.server.context.host.HostParametersChangedEvent
import org.jwebconsole.server.context.host.HostData
import org.jwebconsole.server.context.host.HostDeletedEvent
import java.util.Date
import org.jwebconsole.server.context.common.ResponseMessage

class ThreadCountViewActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {

  sequential

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[ThreadCountDao]
    val probe = TestProbe()
    val probeRef = probe.ref
    dao.exists returns true
    val actor: TestActorRef[ThreadCountViewActor] = TestActorRef(Props(new ThreadCountViewActor(dao)))
    val source = actor.underlyingActor
    val hostId = "test-id"

    def waitForFutureToComplete() {
      Thread.sleep(50)
    }

  }

    "Thread Count View actor" should {
      "replay Host deleted Event" in new mocks {
        val ev = HostDeletedEvent("test-id")
        source.persistEvent isDefinedAt ev must beTrue
      }
    }

    "Thread Count View actor" should {
      "replay Data changed Event" in new mocks {
        val ev = HostDataChangedEvent("test-id", HostData())
        source.persistEvent isDefinedAt ev must beTrue
      }
    }

    "Thread Count View actor" should {
      "not replay unknown events" in new mocks {
        val ev = HostParametersChangedEvent(hostId, "localhost", 8080)
        source.persistEvent isDefinedAt ev must beFalse
      }
    }

    "Thread Count View actor" should {
      "fail silently on DAO error" in new mocks {
        val ev = HostDeletedEvent("test-id")
        dao.deleteHostRecord(anyString) throws new RuntimeException()
        actor ! ev
        waitForFutureToComplete()
        success
      }
    }

  "Thread Count View actor" should {
    "persists host deleted event " in new mocks {
      val ev = HostDeletedEvent("test-id")
      actor ! ev
      waitForFutureToComplete()
      there was one(dao).deleteHostRecord(anyString)
      success
    }
  }

  "Thread Count View actor" should {
    "persists host data changed event" in new mocks {
      val ev = HostDataChangedEvent("test-id", HostData())
      actor ! ev
      waitForFutureToComplete()
      there was one(dao).addThreadCountRecord(anyString, any[ThreadData], any[Date])
      success
    }
  }

  "Thread Count View actor" should {
    "persists host data changed event" in new mocks {
      val ev = HostDataChangedEvent("test-id", HostData())
      actor ! ev
      waitForFutureToComplete()
      there was one(dao).addThreadCountRecord(anyString, any[ThreadData], any[Date])
      success
    }
  }

  "Thread Count View actor" should {
    "respond with error on DAO failure" in new mocks {
      val ev = ThreadDataRequest(hostId)
      dao.getAllForHost(hostId) throws new RuntimeException()
      actor ! ev
      waitForFutureToComplete()
      expectMsg(ResponseMessage(error = Some(ErrorMessages.UnknownErrorMessage)))
    }
  }

  "Thread Count View actor" should {
    "respond with success" in new mocks {
      val ev = ThreadDataRequest(hostId)
      dao.getAllForHost(hostId) returns Nil
      actor ! ev
      waitForFutureToComplete()
      expectMsg(ResponseMessage(body = Some(Nil)))
    }
  }

  "Thread Count View actor" should {
    "respond with error on LastNrRequest when DAO failure" in new mocks {
      val ev = ThreadDataLastNrRequest(hostId, 15)
      dao.getLastNumberOfEntities(hostId, 15) throws new RuntimeException()
      actor ! ev
      expectMsg(ResponseMessage(error = Some(ErrorMessages.UnknownErrorMessage)))
    }
  }

  "Thread Count View actor" should {
    "call DAO base on request records number" in new mocks {
      val ev = ThreadDataLastNrRequest(hostId, 15)
      dao.getAllForHost(hostId) returns Nil
      actor ! ev
      waitForFutureToComplete()
      there was one(dao).getLastNumberOfEntities(hostId, 15)
    }
  }

  "Thread Count View actor" should {
    "respond with success on Last Nr request" in new mocks {
      val ev = ThreadDataLastNrRequest(hostId, 15)
      dao.getLastNumberOfEntities(hostId, 15) returns Nil
      actor ! ev
      waitForFutureToComplete()
      expectMsg(ResponseMessage(body = Some(Nil)))
    }
  }

}
