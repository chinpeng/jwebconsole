package org.jwebconsole.server.readmodel.threads.info

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{ErrorMessages, AkkaTestkitSupport}
import akka.testkit.{TestActorRef, TestProbe}
import akka.actor.Props
import org.jwebconsole.server.context.host._
import org.jwebconsole.server.context.host.HostDataChangedEvent
import org.jwebconsole.server.context.host.HostParametersChangedEvent
import org.jwebconsole.server.context.host.HostData
import org.jwebconsole.server.context.host.HostDeletedEvent
import org.jwebconsole.server.context.common.ResponseMessage

class ThreadInfoViewActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[ThreadInfoDao]
    val probe = TestProbe()
    val probeRef = probe.ref
    dao.exists returns true
    val actor: TestActorRef[ThreadInfoViewActor] = TestActorRef(Props(new ThreadInfoViewActor(dao)))
    val source = actor.underlyingActor
    val hostId = "test-id"

    def waitForFutureToComplete() {
      Thread.sleep(50)
    }

  }

  "Thread Info View actor" should {
    "replay Host deleted Event" in new mocks {
      val ev = HostDeletedEvent("test-id")
      source.persistEvent isDefinedAt ev must beTrue
    }
  }

  "Thread Info View actor" should {
    "replay Data changed Event" in new mocks {
      val ev = HostDataChangedEvent("test-id", HostData())
      source.persistEvent isDefinedAt ev must beTrue
    }
  }

  "Thread Info View actor" should {
    "not replay unknown events" in new mocks {
      val ev = HostParametersChangedEvent(hostId, "localhost", 8080)
      source.persistEvent isDefinedAt ev must beFalse
    }
  }

  "Thread Info View actor" should {
    "fail silently on DAO error" in new mocks {
      val ev = HostDeletedEvent("test-id")
      dao.deleteThreadInfo(anyString) throws new RuntimeException()
      actor ! ev
      success
    }
  }

  "Thread Info View actor" should {
    "persist host deleted event" in new mocks {
      val ev = HostDeletedEvent("test-id")
      actor ! ev
      waitForFutureToComplete()
      there was one(dao).deleteThreadInfo(anyString)
    }
  }

  "Thread Info View actor" should {
    "persist host data changed event" in new mocks {
      val ev = HostDataChangedEvent("test-id", HostData())
      actor ! ev
      waitForFutureToComplete()
      there was one(dao).refreshThreadInfo(anyString, any[ThreadData])
    }
  }

  "Thread Info View actor" should {
    "respond with error when DAO fails" in new mocks {
      val req = ThreadListRequest(hostId)
      dao.threadNamesList(hostId) throws new RuntimeException()
      actor ! req
      waitForFutureToComplete()
      expectMsg(ResponseMessage(error = Some(ErrorMessages.UnknownErrorMessage)))
    }
  }

  "Thread Info View actor" should {
    "respond with success" in new mocks {
      val req = ThreadListRequest(hostId)
      dao.threadNamesList(hostId) returns Nil
      actor ! req
      waitForFutureToComplete()
      expectMsg(ResponseMessage(body = Some(Nil)))
    }
  }


}
