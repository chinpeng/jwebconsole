package org.jwebconsole.server.context.host

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import org.jwebconsole.server.jmx.JMXConnectionChecker
import akka.actor.Props

class HostCommandValidatorSpecs extends Specification with Mockito with NoTimeConversions {

  sequential

  trait mocks extends AkkaTestkitSupport {
    val checker = mock[JMXConnectionChecker]
    val validator = system.actorOf(Props(new HostCommandValidator(checker)))

  }

  "Host Validator" should {
    "respond with successful validation" in new mocks {

    }
  }

}
