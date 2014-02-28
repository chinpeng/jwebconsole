package org.jwebconsole.server.servlet.thread

import akka.actor.ActorSystem
import org.jwebconsole.server.servlet.BaseServlet

class ThreadDataServlet(val system: ActorSystem) extends BaseServlet {

  get("all/:hostId") {
    val hostId = params("hostId")
  }

}
