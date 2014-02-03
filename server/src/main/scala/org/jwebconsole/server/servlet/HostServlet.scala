package org.jwebconsole.server.servlet

import akka.actor._
import org.jwebconsole.server.context.host.{DeleteHostCommand, CreateHostCommand, HostCommandHandler}
import akka.pattern.ask
import org.jwebconsole.server.context.host.model.SimpleHostViewRequest

class HostServlet(override val system: ActorSystem, readModelActor: ActorRef) extends BaseServlet {

  val hostCommandHandler = system.actorOf(Props[HostCommandHandler])


  before() {
    contentType = formats("json")
  }

  post("/listen") {
    val cmd = parsedBody.extract[CreateHostCommand]
    val valid = hostCommandHandler ? cmd
    executeAsync(valid)
  }

  delete("/delete/:id") {
    val id = params("id")
    val cmd = DeleteHostCommand(id)
    val result = hostCommandHandler ? cmd
    executeAsync(result)
  }

  get("/status/all") {
    executeAsync(readModelActor ? SimpleHostViewRequest)
  }

  case class SampleCmd(name: String, port: Int)

}
