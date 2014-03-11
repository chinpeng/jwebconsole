package org.jwebconsole.server.servlet.thread

import akka.actor.{ActorRef, ActorSystem}
import org.jwebconsole.server.servlet.BaseServlet
import org.jwebconsole.server.readmodel.threads.details.ThreadDetailsRequest
import akka.pattern.ask

class ThreadDetailsServlet(val system: ActorSystem, threadDetailsView: ActorRef) extends BaseServlet {

  get("/:hostId/:threadId") {
    val hostId = params("hostId")
    val threadId = params("threadId").toLong
    val req = ThreadDetailsRequest(hostId, threadId)
    executeAsync(threadDetailsView ? req)
  }

}
