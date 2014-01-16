package org.jwebconsole.server.jmx

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito

class JMXConnectionReceiverSpecs extends Specification with Mockito {

  "JMX Connection Receiver" should {
    "make correct connection URL" in {
      val receiver = new JMXConnectionReceiver
      val url = receiver.createConnectionURL("localhost", 8080)
      url.toString must beMatching("service:jmx:rmi:///jndi/rmi://localhost:8080/jmxrmi")
    }
  }

}
