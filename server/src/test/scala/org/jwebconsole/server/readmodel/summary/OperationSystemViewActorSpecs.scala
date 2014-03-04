package org.jwebconsole.server.readmodel.summary

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{ErrorMessages, AkkaTestkitSupport}
import akka.testkit.{TestActorRef, TestProbe}
import akka.actor.Props
import org.jwebconsole.server.readmodel.summary.os.{OperationSystemInfoRequest, OperationSystemViewActor, OperationSystemDao}
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
class OperationSystemViewActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[OperationSystemDao]
    val probe = TestProbe()
    val probeRef = probe.ref
    dao.exists returns true
    val actor: TestActorRef[OperationSystemViewActor] = TestActorRef(Props(new OperationSystemViewActor(dao)))
    val source = actor.underlyingActor
    val hostId = "test-id"
    val hostName = "localhost"
    val hostPort = 8080

    def waitForFutureToComplete() {
      Thread.sleep(50)
    }
  }

  "Operation System View actor" should {
    "replay Host deleted Event" in new mocks {
      source.filterFunc(HostDeletedEvent(hostId)) must beTrue
    }
  }

  "Operation System View actor" should {
    "replay Data changed Event" in new mocks {
      source.filterFunc(HostDataChangedEvent(hostId, HostData())) must beTrue
    }
  }

  "Operation System View actor" should {
    "not replay unknown events" in new mocks {
      source.filterFunc(HostParametersChangedEvent(hostId, hostName, hostPort)) must beFalse
      source.filterFunc(HostCreatedEvent(hostId, hostName, hostPort)) must beFalse
    }
  }

  "Operation System View actor" should {
    "fail silently on DAO error" in new mocks {
      dao.deleteOperationSystemInfo(anyString) throws new RuntimeException()
      actor ! HostDeletedEvent(hostId)
      success
    }
  }

  "Operation System View actor" should {
    "persist host deleted event" in new mocks {
      actor ! HostDeletedEvent(hostId)
      waitForFutureToComplete()
      there was one(dao).deleteOperationSystemInfo(anyString)
    }
  }

  "Operation System View actor" should {
    "persist host data changed event" in new mocks {
      actor ! HostDataChangedEvent(hostId, HostData())
      waitForFutureToComplete()
      there was one(dao).refreshOperationSystemInfo(anyString, any[OperationSystemData])
    }
  }

  "Operation System View actor" should {
    "respond with error when DAO fails" in new mocks {
      val req = OperationSystemInfoRequest(hostId)
      dao.getOperationSystemInfo(hostId) throws new RuntimeException()
      actor ! req
      waitForFutureToComplete()
      expectMsg(ResponseMessage(error = Some(ErrorMessages.DbConnectionFailureMessage)))
    }
  }

}
