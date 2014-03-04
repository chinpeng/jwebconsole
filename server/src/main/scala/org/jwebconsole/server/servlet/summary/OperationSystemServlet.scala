package org.jwebconsole.server.servlet.summary

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import org.jwebconsole.server.servlet.BaseServlet
import org.jwebconsole.server.readmodel.summary.os.OperationSystemInfoRequest

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 18:12
 */
class OperationSystemServlet(val system: ActorSystem, operationSystemInfoActor: ActorRef) extends BaseServlet {
  get("/get/:hostId") {
    val hostId = params("hostId")
    val req = OperationSystemInfoRequest(hostId)
    executeAsync(operationSystemInfoActor ? req)
  }
}
