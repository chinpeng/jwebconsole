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
import org.scalatra.CorsSupport

class DemoServlet(override val system: ActorSystem) extends GlueActorServlet {

  val hostActor = system.actorOf(HostManagerActor())

  before() {
    contentType = formats("json")
  }

  post("/listen") {
    val host = parsedBody.extract[HostInfo]
    val front = system.actorOf(GlueActor())
    front ! ConnectHost(host)
    HostConnected(host)
  }

  get("/status/:host/:port") {
    val host = HostInfo(params("host"), params("port").toInt)
    val front = system.actorOf(GlueActor())
    val status: Future[JMXHostStatus] = (front ? GetServerStatus(host)).asInstanceOf[Future[JMXHostStatus]]
    executeAsync(status)
  }

  get("/status/all") {
    val front = system.actorOf(GlueActor())
    val hosts: Future[ConnectedHostsResponse] = (front ? GetConnectedHost()).asInstanceOf[Future[ConnectedHostsResponse]]
    executeAsync(hosts)
  }


}
