package org.jwebconsole.server.readmodel.hostlist

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{ErrorMessages, AkkaTestkitSupport}
import akka.testkit.{TestProbe, TestActorRef}
import akka.actor.Props
import org.jwebconsole.server.context.host._
import org.specs2.mock.mockito.ArgumentCapture
import org.jwebconsole.server.context.host.HostDataChangedEvent
import org.jwebconsole.server.context.host.HostCreatedEvent
import org.jwebconsole.server.context.host.HostData
import org.jwebconsole.server.context.host.HostDeletedEvent
import java.sql.SQLException
import org.jwebconsole.server.context.common.ResponseMessage

class HostListViewActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[SimpleHostDao]
    dao.exists returns true
    val actor: TestActorRef[HostListViewActor] = TestActorRef(Props(new HostListViewActor(dao)))
    val source = actor.underlyingActor
    val hostId = "test-id"
    val probe = TestProbe()
    val probeRef = probe.ref
    val host = SimpleHostView(hostId, "localhost", 8080, "test-login", "test-password")

    def waitForPersist() {
      Thread.sleep(100)
    }

  }

  "Host List View actor" should {
    "replay Host Data Change Events" in new mocks {
      val ev = HostDataChangedEvent(hostId, HostData())
      source.persistEvent isDefinedAt (ev) must beTrue
    }
  }

  "Host List View actor" should {
    "replay host changed events" in new mocks {
      val ev = HostDeletedEvent(hostId)
      source.persistEvent isDefinedAt (ev) must beTrue
    }
  }

  "Host List View actor" should {
    "fire hosts list event on finish recover" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[AvailableHostsList])
      source.afterRecover()
      probe.expectMsgPF() {
        case ev: AvailableHostsList => true
      }
    }
  }

  "Host ListView actor" should {
    "persist replaying HostCreatedEvent" in new mocks {
      val ev = HostCreatedEvent(hostId, "", 0)
      source.persistEvent(ev)
      val captor = new ArgumentCapture[SimpleHostView]
      there was one(dao).create(captor.capture)
      captor.value.id mustEqual "test-id"
    }
  }

  "Host ListView actor" should {
    "persist replaying HostParametersChangedEvent" in new mocks {
      val ev = HostParametersChangedEvent(hostId, "", 0)
      source.persistEvent(ev)
      val captor = new ArgumentCapture[SimpleHostView]
      there was one(dao).updateParameters(captor.capture)
      captor.value.id mustEqual "test-id"
    }
  }

  "Host ListView actor" should {
    "persist replaying HostDeletedEvent" in new mocks {
      val ev = HostDeletedEvent(hostId)
      source.persistEvent(ev)
      there was one(dao).delete(hostId)
    }
  }

  "Host ListView actor" should {
    "persist Host status change replaying event" in new mocks {
      val ev = HostDataChangedEvent(hostId, HostData(connected = true))
      source.persistEvent(ev)
      there was one(dao).updateStatus(hostId, true)
    }
  }

  "Host ListView actor" should {
    "persist HostCreatedEvent" in new mocks {
      val ev = HostCreatedEvent(hostId, "", 0)
      actor ! ev
      waitForPersist()
      val captor = new ArgumentCapture[SimpleHostView]
      there was one(dao).create(captor.capture)
      captor.value.id mustEqual "test-id"
    }
  }

  "Host ListView actor" should {
    "persist replaying HostParametersChangedEvent" in new mocks {
      val ev = HostParametersChangedEvent(hostId, "", 0)
      actor ! ev
      waitForPersist()
      val captor = new ArgumentCapture[SimpleHostView]
      there was one(dao).updateParameters(captor.capture)
      captor.value.id mustEqual "test-id"
    }
  }

  "Host ListView actor" should {
    "persist replaying HostDeletedEvent" in new mocks {
      val ev = HostDeletedEvent(hostId)
      actor ! ev
      waitForPersist()
      there was one(dao).delete(hostId)
    }
  }

  "Host ListView actor" should {
    "persist Host status change replaying event" in new mocks {
      val ev = HostDataChangedEvent(hostId, HostData(connected = true))
      actor ! ev
      waitForPersist()
      there was one(dao).updateStatus(hostId, true)
    }
  }

  "Host ListView actor" should {
    "should handle DB failure silently" in new mocks {
      val ev = HostDataChangedEvent(hostId, HostData(connected = true))
      dao.updateStatus(hostId, true) throws new RuntimeException
      actor ! ev
      success
    }
  }

  "Host ListView actor" should {
    "respond with error on db failure" in new mocks {
      dao.getAll throws new SQLException()
      actor ! SimpleHostViewListRequest
      expectMsgPF() {
        case msg: ResponseMessage if msg.error == Some(ErrorMessages.DbConnectionFailureMessage) => true
      }
    }
  }

  "Host ListView actor" should {
    "respond with error on host not found" in new mocks {
      dao.getSingle(anyString) throws new NoSuchElementException
      actor ! SimpleHostViewRequest(hostId)
      expectMsgPF() {
        case msg: ResponseMessage if msg.error == Some(ErrorMessages.HostNotFoundMessage) => true
      }
    }
  }

  "Host ListView actor" should {
    "respond with error on any unknown exception" in new mocks {
      dao.getSingle(anyString) throws new RuntimeException
      actor ! SimpleHostViewRequest(hostId)
      expectMsgPF() {
        case msg: ResponseMessage if msg.error == Some(ErrorMessages.UnknownErrorMessage) => true
      }
    }
  }

  "Host ListView actor" should {
    "should respond without login and password on single host request" in new mocks {
      dao.getSingle(hostId) returns host
      actor ! SimpleHostViewRequest(hostId)
      expectMsg(ResponseMessage(body = Some(host.copy(login = "", password = ""))))
    }
  }

  "Host ListView actor" should {
    "should respond without login and password" in new mocks {
      dao.getAll returns List(host)
      actor ! SimpleHostViewListRequest
      expectMsg(ResponseMessage(body = Some(List(host.copy(login = "", password = "")))))
    }
  }

  "Host ListView actor" should {
    "should sort by id" in new mocks {
      dao.getAll returns List(host.copy(id = "2"), host.copy(id = "1"))
      actor ! SimpleHostViewListRequest
      expectMsg(ResponseMessage(body = Some(List(
        host.copy(id = "1", login = "", password = ""),
        host.copy(id = "2", login = "", password = "")
      ))))
    }
  }

}
