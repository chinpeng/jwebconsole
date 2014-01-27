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

class DemoServlet(override val system: ActorSystem) extends BaseServlet {

  val hostActor = system.actorOf(HostManagerActor())

  before() {
    contentType = formats("json")
  }

  post("/listen") {

  }

  get("/status/:host/:port") {

  }

  get("/status/all") {

  }


}
