package org.jwebconsole.server.servlet

import akka.actor._
import org.jwebconsole.server.context.host.{ChangeHostCommand, DeleteHostCommand, CreateHostCommand, HostCommandHandler}
import akka.pattern.ask
import org.jwebconsole.server.context.host.model.SimpleHostViewRequest

class HostServlet(override val system: ActorSystem, readModelActor: ActorRef) extends BaseServlet {

  val hostCommandHandler = system.actorOf(Props[HostCommandHandler])


  before() {
    contentType = formats("json")
  }

  get("/all") {
    executeAsync(readModelActor ? SimpleHostViewRequest)
  }

  post("/add") {
    val cmd = parsedBody.extract[CreateHostCommand]
    val valid = hostCommandHandler ? cmd
    executeAsync(valid)
  }

  put("/edit/:id") {
    val cmd = parsedBody.extract[ChangeHostCommand]
    val cmdWithId = cmd.copy(id = params("id"))
    val valid = hostCommandHandler ? cmdWithId
    executeAsync(valid)
  }

  delete("/delete/:id") {
    val id = params("id")
    val cmd = DeleteHostCommand(id)
    val result = hostCommandHandler ? cmd
    executeAsync(result)
  }

}
