package org.jwebconsole.server.servlet.thread

import akka.actor._
import akka.pattern.ask
import org.jwebconsole.server.servlet.BaseServlet
import org.jwebconsole.server.readmodel.threads.info.ThreadListRequest

class ThreadInfoServlet(val system: ActorSystem, val threadInfoActor: ActorRef) extends BaseServlet {

  get("/all/:hostId") {
    val hostId = params("hostId")
    val req = ThreadListRequest(hostId)
    executeAsync(threadInfoActor ? req)
  }

}
