package org.jwebconsole.server.readmodel.threads.details

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import akka.testkit.{TestActorRef, TestProbe}
import akka.actor.Props
import org.jwebconsole.server.context.host.{HostParametersChangedEvent, HostData, HostDataChangedEvent, HostDeletedEvent}
import org.jwebconsole.server.readmodel.threads.info.ThreadListRequest
import scala.util.Try

class ThreadDetailsViewActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {

  sequential

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[ThreadDetailsDao]
    val probe = TestProbe()
    val probeRef = probe.ref
    dao.exists returns true
    val actor: TestActorRef[ThreadDetailsViewActor] = TestActorRef(Props(new ThreadDetailsViewActor(dao)))
    val source = actor.underlyingActor
    val hostId = "test-id"

    def waitForFutureToComplete() {
      Thread.sleep(50)
    }

  }

  "Thread Details View actor" should {
    "replay Host deleted Event" in new mocks {
      val ev = HostDeletedEvent("test-id")
      source.persistEvent(ev)
      there was one(dao).deleteRecordsForHost("test-id")
    }
  }

  "Thread Details View actor" should {
    "replay Host Data changed Event" in new mocks {
      val data = HostData()
      val ev = HostDataChangedEvent("test-id", data)
      source.persistEvent(ev)
      there was one(dao).refreshRecordsForHost("test-id", data.threadData.availableThreads)
    }
  }

  "Thread Details View actor" should {
    "not replay unknown events" in new mocks {
      val data = HostData()
      val ev = HostParametersChangedEvent("test-id", "localhost", 8080)
      source.persistEvent isDefinedAt ev must beFalse
    }
  }

  "Thread Details View actor" should {
    "respond with data on request" in new mocks {
      dao.getRecordsForThread("test-id", 10) returns Nil
      val result = source.processRequest(ThreadDetailsRequest("test-id", 10))
      result mustEqual Nil
    }
  }

  "Thread Details View actor" should {
    "should not handle unknown requests" in new mocks {
      dao.getRecordsForThread("test-id", 10) returns Nil
      Try(source.processRequest(ThreadListRequest("test-id"))).isFailure must beTrue
    }
  }

}
