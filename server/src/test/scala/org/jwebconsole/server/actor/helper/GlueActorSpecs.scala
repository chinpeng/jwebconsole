package org.jwebconsole.server.actor.helper

import org.specs2.mutable.{BeforeAfter, Specification}
import org.specs2.mock.Mockito
import akka.testkit.{TestProbe, TestActorRef}
import org.jwebconsole.server.actor.provider.ActorProvider
import org.jwebconsole.server.model.{HostConnected, GetServerStatus, HostInfo}
import akka.actor.{ActorRef, ActorSystem}
import org.jwebconsole.server.util.AppConstants
import org.jwebconsole.server.actor.model.StopActor

class GlueActorSpecs extends Specification with Mockito {

  trait mocks extends BeforeAfter {
    implicit val system = ActorSystem("spec")
    val provider = mock[ActorProvider]
    val actorRef: TestActorRef[GlueActor] = TestActorRef(new GlueActor(provider))
    val actor: GlueActor = actorRef.underlyingActor
    val publishMessage = GetServerStatus(HostInfo("localhost", 8080))
    val mockRef: ActorRef = spy(TestProbe().ref)
    val responseMessage = HostConnected(HostInfo("localhost", 8080))

    def before: Unit = {
      provider.senderRef returns mockRef
      provider.selfRef returns actorRef
    }
    def after(): Unit = {
      system.shutdown()
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
    "stop self after successful executing" in new mocks {
      actor.receive(publishMessage)
      actor.receive(responseMessage)
      there was one(provider).stopSelf()
    }
  }

  "Glue actor" should {
    "not stop self after publishing event" in new mocks {
      actor.receive(publishMessage)
      there was no(provider).stopSelf()
    }
  }

  "Glue actor" should {
    "store sender after publishing event" in new mocks {
      actor.receive(publishMessage)
      actor.pending mustEqual mockRef
    }
  }

  "Glue actor" should {
    "send failed message when not receiving response" in new mocks {
      actor.receive(publishMessage)
      there was one(provider).scheduleMessageOnce(AppConstants.GlueActorTimeOut, actorRef, StopActor)
    }
  }


}
