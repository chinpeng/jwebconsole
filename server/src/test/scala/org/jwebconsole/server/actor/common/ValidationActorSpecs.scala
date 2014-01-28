package org.jwebconsole.server.actor.common

import org.specs2.mutable.{BeforeAfter, Specification}
import org.specs2.mock.Mockito
import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{TestProbe, TestActorRef}
import org.jwebconsole.server.util._
import org.jwebconsole.server.util.Valid
import org.jwebconsole.server.util.Invalid
import org.jwebconsole.server.context.util.{ValidationFailed, ValidationAck, ValidationWithSender, ValidationActor}


class ValidationActorSpecs extends Specification with Mockito {

  trait mocks extends BeforeAfter {
    implicit val system = ActorSystem("test-system")
    val ref: TestActorRef[ValidationActor] = TestActorRef(new ValidationActor())
    val actor = ref.underlyingActor
    val senderRef: ActorRef = spy(TestProbe().ref)
    val msg = InvalidMessage(1, "Validation failed")

    def before: Unit = {}

    def after: Unit = {
      system.shutdown()
    }
  }

 /*  "Validation actor" should {
    "respond to sender with successful validation" in new mocks {
      actor.receive(ValidationWithSender(senderRef, Valid(Nil)))
      there was one(senderRef).tell(ValidationAck, ref)
    }
  }

  "Validation actor" should {
    "respond to sender with failed validation" in new mocks {
      actor.receive(ValidationWithSender(senderRef, Invalid(Nil, List(msg))))
      there was one(senderRef).tell(ValidationFailed(List(msg)), ref)
    }
  }

  "Validation actor" should {
    "handle unknown messages" in new mocks {
      actor.receive.isDefinedAt(Nil) mustEqual true
    }
  }
*/

}
