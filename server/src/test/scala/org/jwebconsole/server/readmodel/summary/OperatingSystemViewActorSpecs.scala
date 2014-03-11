package org.jwebconsole.server.readmodel.summary

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{ErrorMessages, AkkaTestkitSupport}
import akka.testkit.{TestActorRef, TestProbe}
import akka.actor.Props
import org.jwebconsole.server.readmodel.summary.os.{OperatingSystemInfoRequest, OperatingSystemViewActor, OperatingSystemDao}
import org.jwebconsole.server.context.host._
import org.jwebconsole.server.context.host.HostDataChangedEvent
import org.jwebconsole.server.context.host.HostParametersChangedEvent
import org.jwebconsole.server.context.host.HostData
import org.jwebconsole.server.context.host.HostDeletedEvent
import org.jwebconsole.server.context.common.ResponseMessage

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 20:12
 */
class OperatingSystemViewActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[OperatingSystemDao]
    val probe = TestProbe()
    val probeRef = probe.ref
    dao.exists returns true
    val actor: TestActorRef[OperatingSystemViewActor] = TestActorRef(Props(new OperatingSystemViewActor(dao)))
    val source = actor.underlyingActor
    val hostId = "test-id"
    val hostName = "localhost"
    val hostPort = 8080

    def waitForFutureToComplete() {
      Thread.sleep(50)
    }
  }

  "Operating System View actor" should {
    "replay Host deleted Event" in new mocks {
      val ev = HostDeletedEvent(hostId)
      source.persistEvent isDefinedAt ev must beTrue
    }
  }

  "Operating System View actor" should {
    "replay Data changed Event" in new mocks {
      val ev = HostDataChangedEvent(hostId, HostData())
      source.persistEvent isDefinedAt ev must beTrue
    }
  }

  "Operating System View actor" should {
    "not replay unknown events" in new mocks {
      val changeEv = HostParametersChangedEvent(hostId, hostName, hostPort)
      val createEv = HostCreatedEvent(hostId, hostName, hostPort)
      source.persistEvent isDefinedAt changeEv must beFalse
      source.persistEvent isDefinedAt createEv must beFalse
    }
  }

  "Operating System View actor" should {
    "fail silently on DAO error" in new mocks {
      dao.deleteOperatingSystemInfo(anyString) throws new RuntimeException()
      actor ! HostDeletedEvent(hostId)
      success
    }
  }

  "Operating System View actor" should {
    "persist host deleted event" in new mocks {
      actor ! HostDeletedEvent(hostId)
      waitForFutureToComplete()
      there was one(dao).deleteOperatingSystemInfo(anyString)
    }
  }

  "Operating System View actor" should {
    "persist host data changed event" in new mocks {
      actor ! HostDataChangedEvent(hostId, HostData())
      waitForFutureToComplete()
      there was one(dao).refreshOperatingSystemInfo(anyString, any[OperatingSystemData])
    }
  }

  "Operating System View actor" should {
    "respond with error when DAO fails" in new mocks {
      val req = OperatingSystemInfoRequest(hostId)
      dao.getOperatingSystemInfo(hostId) throws new RuntimeException()
      actor ! req
      waitForFutureToComplete()
      expectMsg(ResponseMessage(error = Some(ErrorMessages.UnknownErrorMessage)))
    }
  }

}
