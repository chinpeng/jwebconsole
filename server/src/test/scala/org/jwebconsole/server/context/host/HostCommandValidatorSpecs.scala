package org.jwebconsole.server.context.host

import org.specs2.mutable.{SpecificationWithJUnit, Specification}
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.{InvalidMessage, Invalid, Valid, AkkaTestkitSupport}
import org.jwebconsole.server.jmx.JMXConnectionChecker
import akka.actor.Props
import org.jwebconsole.server.util.ValidationConstants._

class HostCommandValidatorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {

  sequential

  trait mocks extends AkkaTestkitSupport {
    val checker = mock[JMXConnectionChecker]
    val validator = system.actorOf(Props(new HostCommandValidator(checker)))
    var model = HostStateModel("test-id", "localhost", 8080, deleted = false)

    def expectSuccess = expectMsgPF() {
      case Valid(_) => true
    }

    def expectFailure(msg: InvalidMessage) = expectMsgPF() {
      case Invalid(_, items) if items.head.id == msg.id => true
    }

  }

  "Host Validator" should {
    "respond with successful validation" in new mocks {
      validator ! ValidateHost(model, ChangeHostCommand("test-id", "newName", 8080))
      expectSuccess
    }
  }

  "Host Validator" should {
    "validate negative port" in new mocks {
      validator ! ValidateHost(model, ChangeHostCommand("test-id", "newName", -8080))
      expectFailure(PortMustBePositive)
    }
  }

  "Host Validator" should {
    "validate empty port" in new mocks {
      validator ! ValidateHost(model, ChangeHostCommand("test-id", "newName", 0))
      expectFailure(PortMustBePositive)
    }
  }

  "Host validator" should {
    "validate empty host" in new mocks {
      validator ! ValidateHost(model, ChangeHostCommand("test-id", "", 10))
      expectFailure(HostEmptyMessage)
    }
  }

  "Host validator" should {
    "validate host when it's unreachable" in new mocks {
      checker.checkConnection(anyString, anyInt) throws new RuntimeException
      validator ! ValidateHost(model, ChangeHostCommand("test-id", "test", 10))
      expectFailure(UnableToConnectMessage)
    }
  }

  "Host validator" should {
    "respond with sequence of validations" in new mocks {
      validator ! ValidateHost(model, ChangeHostCommand("test-id", "", -10))
      expectMsgPF() {
        case Invalid(_, msgs) if msgs.size == 2 => true
      }
    }
  }

}
