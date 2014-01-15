package org.jwebconsole.server.servlet

import akka.actor.{Props, ActorRef}
import org.jwebconsole.server.actor.{Check, SampleActor}
import akka.pattern.ask
import org.scalatra.AsyncResult
import org.jwebconsole.server.actor.memory.{GetMemory, MemoryUsageHolderActor}

class DemoServlet extends DefaultServlet {

  var memoryActor: ActorRef = null

  before() {
    contentType = formats("json")
    if (memoryActor == null) {
      memoryActor = system.actorOf(Props[MemoryUsageHolderActor])
    }
  }

  get("/") {
    executeAsync(memoryActor ? GetMemory)
  }

}
