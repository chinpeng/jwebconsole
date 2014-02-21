package org.jwebconsole.server.servlet

import akka.actor._
import akka.pattern.ask
import org.jwebconsole.server.readmodel.threads.{ThreadDataLastNrRequest, ThreadDataRequest}

class ThreadDataServlet(val system: ActorSystem, threadDataView: ActorRef) extends BaseServlet {

  before() {
    contentType = formats("json")
  }

  get("/:id/all") {
    val id = params("id")
    val req = ThreadDataRequest(id)
    executeAsync(threadDataView ? req)
  }

  get("/last/:number/:hostId") {
    val id = params("hostId")
    val number = params("number").toInt
    val req = ThreadDataLastNrRequest(id, number)
    executeAsync(threadDataView ? req)
  }

}
