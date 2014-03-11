package org.jwebconsole.server.servlet.thread

import akka.actor._
import akka.pattern.ask
import org.jwebconsole.server.readmodel.threads.count.{ThreadDataLastNrRequest, ThreadDataRequest}
import org.jwebconsole.server.servlet.BaseServlet

class ThreadCountServlet(val system: ActorSystem, threadCountView: ActorRef) extends BaseServlet {

  get("/all/:hostId") {
    val id = params("hostId")
    val req = ThreadDataRequest(id)
    executeAsync(threadCountView ? req)
  }

  get("/last/:number/:hostId") {
    val id = params("hostId")
    val number = params("number").toInt
    val req = ThreadDataLastNrRequest(id, number)
    executeAsync(threadCountView ? req)
  }

}
