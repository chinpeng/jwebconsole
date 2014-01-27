package org.jwebconsole.server.actor.common

import org.specs2.mutable._
import org.specs2.time.NoTimeConversions

import akka.actor._
import akka.testkit._
import scala.concurrent.duration._

abstract class AkkaTestkitSpecs2Support extends TestKit(ActorSystem())
with After
with ImplicitSender {
  def after = system.shutdown()
}
class ExampleSpec extends Specification with NoTimeConversions {
  sequential

  "A TestKit" should {
    "work properly with Specs2 unit tests" in new AkkaTestkitSpecs2Support {
      within(1 second) {
        system.actorOf(Props(new Actor {
          def receive = { case x ⇒ sender ! x }
        })) ! "hallo"
        expectMsgType[String] must be equalTo "hallo"
      }
    }
  }
}