package org.jwebconsole.server.servlet

import akka.actor._
import akka.pattern.ask
import org.jwebconsole.server.actor.server.HostManagerActor
import scala.concurrent.Future
import org.jwebconsole.server.model._
import org.jwebconsole.server.model.JMXHostStatus
import org.jwebconsole.server.model.HostInfo
import org.jwebconsole.server.model.GetServerStatus
import org.jwebconsole.server.actor.helper.GlueActor

class DemoServlet(override val system: ActorSystem) extends GlueActorServlet {

  val hostActor = system.actorOf(Props[HostManagerActor])


  before() {
    contentType = formats("json")
  }

  post("/listen") {
    val host = parsedBody.extract[HostInfo]
    val front = system.actorOf(GlueActor())
    val status: Future[HostConnected] = (front ? ConnectHost(host)).asInstanceOf[Future[HostConnected]]
    executeAsync(status)
  }

  get("/status/:host/:port") {
    val host = HostInfo(params("host"), params("port").toInt)
    val front = system.actorOf(GlueActor())
    val status: Future[JMXHostStatus] = (front ? GetServerStatus(host)).asInstanceOf[Future[JMXHostStatus]]
    executeAsync(status)
  }



}
