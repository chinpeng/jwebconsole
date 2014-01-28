package org.jwebconsole.server.servlet

import akka.actor._
import org.jwebconsole.server.context.host.{CreateHostCommand, HostCommandHandler}
import akka.pattern.ask

class DemoServlet(override val system: ActorSystem) extends BaseServlet {

  val hostCommandHandler = system.actorOf(Props[HostCommandHandler])

  before() {
    contentType = formats("json")
  }

  post("/listen") {
    val cmd = parsedBody.extract[CreateHostCommand]
    val valid = hostCommandHandler ? cmd
    executeAsync(valid)
  }

  get("/status/:id") {

  }

  get("/status/all") {

  }


}
