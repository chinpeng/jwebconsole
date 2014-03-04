package org.jwebconsole.server.servlet.summary

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import org.jwebconsole.server.servlet.BaseServlet
import org.jwebconsole.server.readmodel.summary.os.OperatingSystemInfoRequest

/**
 * Created by amednikov
 * Date: 04.03.14
 * Time: 18:12
 */
class OperatingSystemServlet(val system: ActorSystem, operatingSystemInfoActor: ActorRef) extends BaseServlet {
  get("/get/:hostId") {
    val hostId = params("hostId")
    val req = OperatingSystemInfoRequest(hostId)
    executeAsync(operatingSystemInfoActor ? req)
  }
}
