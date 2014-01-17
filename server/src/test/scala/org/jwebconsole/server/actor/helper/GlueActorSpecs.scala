package org.jwebconsole.server.actor.helper

import org.specs2.mutable.{Before, Specification}
import org.specs2.mock.Mockito
import akka.testkit.TestActorRef
import org.jwebconsole.server.actor.provider.ActorProvider
import org.jwebconsole.server.model.{HostConnected, GetServerStatus, HostInfo}
import akka.actor.{Actor, ActorRef, ActorSystem}

class GlueActorSpecs extends Specification with Mockito {

  trait mocks extends Before {
    implicit val system = ActorSystem("spec")
    val provider = mock[ActorProvider]
    val actor: GlueActor = TestActorRef(new GlueActor(provider)).underlyingActor
    val publishMessage = GetServerStatus(HostInfo("localhost", 8080))
    val mockRef: ActorRef = spy(TestActorRef(new TestActor))
    val responseMessage = HostConnected(HostInfo("localhost", 8080))
    def before: Unit = {
      provider.senderRef returns mockRef
    }
  }

  "Glue actor" should {
    "publish received event" in new mocks {
      actor.receive(publishMessage)
      there was one(provider).publishEvent(any[Any])
    }
  }

  "Glue actor" should {
    "return response message to sender" in new mocks {
      actor.receive(publishMessage)
      actor.receive(responseMessage)
      there was one(mockRef).tell(any[HostConnected], any[ActorRef])
    }
  }

  "Glue actor" should {
    "stop self after successful executing" in new mocks{
      actor.receive(publishMessage)
      actor.receive(responseMessage)
      there was one(provider).stopSelf()
    }
  }

  class TestActor extends Actor {
    def receive: Actor.Receive = {
      case _ => Unit
    }
  }
}
