package org.jwebconsole.server.servlet

import akka.actor._
import akka.pattern.ask
import org.jwebconsole.server.readmodel.threads.ThreadDataRequest

class ThreadDataServlet(val system: ActorSystem, threadDataView: ActorRef) extends BaseServlet {

  before() {
    contentType = formats("json")
  }

  get("/all/:id") {
    val id = params("id")
    val req = ThreadDataRequest(id)
    executeAsync(threadDataView ? req)
  }


}
