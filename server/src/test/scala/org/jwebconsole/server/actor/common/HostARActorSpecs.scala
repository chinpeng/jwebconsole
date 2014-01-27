package org.jwebconsole.server.actor.common

import org.specs2.mutable.Specification
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{Valid, Invalid, AkkaTestkitSupport}
import akka.actor.Props
import org.jwebconsole.server.context.host.{HostCommand, CreateHostCommand, WithSender, HostARActor}
import akka.testkit.TestProbe
import org.jwebconsole.server.context.util.ValidationWithSender

class HostARActorSpecs extends Specification with NoTimeConversions {
  sequential

  trait mocks extends AkkaTestkitSupport {
    val processor =  system.actorOf(Props(new HostARActor("1")))
    val probe = TestProbe()
    val probeRef = probe.ref
    val id = 1
    val port = 8080
    val name = "localhost"

    def expectValidationFailed() {
      probe.expectMsgPF() {
        case ValidationWithSender(_, Invalid(_, _)) => true
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



}
