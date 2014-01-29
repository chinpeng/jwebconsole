package org.jwebconsole.server.context.host

import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{Valid, Invalid, AkkaTestkitSupport}
import akka.actor.Props
import akka.testkit.TestProbe
import org.jwebconsole.server.context.common.{ResponseMessage, ValidationWithSender}

class HostARActorSpecs extends Specification with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val processor = system.actorOf(Props(new HostARActor("1")))
    val probe = TestProbe()
    val probeRef = probe.ref
    val eventProbe = TestProbe()
    val eventProbeRef = eventProbe.ref
    val id = "test-id"
    val port = 8080
    val name = "localhost"

    def expectValidationFailed() {
      probe.expectMsgPF() {
        case ResponseMessage(_, msgs, _) if !msgs.isEmpty => true
      }
    }

    def expectValidationSuccess() {
      probe.expectMsgPF() {
        case ResponseMessage(_, msgs, _) if msgs.isEmpty => true
      }
    }

    def pushCommand(cmd: HostCommand) {
      processor ! WithSender(probeRef, cmd)
    }

  }

  "host AR actor" should {
    "validate invalid port" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[ValidationWithSender[Any]])
      pushCommand(new CreateHostCommand(id, name, -10))
      expectValidationFailed()
    }
  }
  "host AR actor" should {
    "validate null port" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[ValidationWithSender[Any]])
      pushCommand(new CreateHostCommand(id, name, null.asInstanceOf[Int]))
      expectValidationFailed()
    }
  }

  "host AR actor" should {
    "validate empty host name" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[ValidationWithSender[Any]])
      processor ! WithSender(probeRef, new CreateHostCommand(id, "", port))
      expectValidationFailed()
    }
  }

  "host AR actor" should {
    "validate null host name" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[ValidationWithSender[Any]])
      processor ! WithSender(probeRef, new CreateHostCommand(id, null, port))
      expectValidationFailed()
    }
  }

  "host AR actor" should {
    "not receive change command first" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[ValidationWithSender[Any]])
      processor ! WithSender(probeRef, new ChangeHostCommand(id, name, port))
      expectValidationFailed()
    }
  }

  "host AR actor" should {
    "not receive delete command first" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[ValidationWithSender[Any]])
      processor ! WithSender(probeRef, new DeleteHostCommand(id))
      expectValidationFailed()
    }
  }

  "host AR actor" should {
    "not receive create command twice" in new mocks {
      system.eventStream.subscribe(probeRef, classOf[ValidationWithSender[Any]])
      processor ! WithSender(probeRef, new CreateHostCommand(id, name, port))
      expectValidationSuccess()
      processor ! WithSender(probeRef, new CreateHostCommand(id, name, port))
      expectValidationFailed()
    }
  }

  "host AR actor" should {
    "publish created event" in new mocks {
      system.eventStream.subscribe(eventProbeRef, classOf[HostCreatedEvent])
      processor ! WithSender(probeRef, new CreateHostCommand(id, name, port))
      eventProbe.expectMsg(HostCreatedEvent(id, name, port))
    }
  }

  "host AR actor" should {
    "publish changed event" in new mocks {
      system.eventStream.subscribe(eventProbeRef, classOf[HostParametersChangedEvent])
      processor ! WithSender(probeRef, new CreateHostCommand(id, name, port))
      processor ! WithSender(probeRef, new ChangeHostCommand(id, name, port))
      eventProbe.expectMsg(HostParametersChangedEvent(id, name, port))
    }
  }

  "host AR actor" should {
    "publish deleted event" in new mocks {
      system.eventStream.subscribe(eventProbeRef, classOf[HostDeletedEvent])
      processor ! WithSender(probeRef, new CreateHostCommand(id, name, port))
      processor ! WithSender(probeRef, new DeleteHostCommand(id))
      eventProbe.expectMsg(HostDeletedEvent(id))
    }
  }

}
