package org.jwebconsole.server.servlet

import akka.actor._
import org.jwebconsole.server.context.host._
import akka.pattern.ask
import org.jwebconsole.server.jmx.JMXConnectionChecker
import org.jwebconsole.server.context.host.ChangeHostCommand
import org.jwebconsole.server.context.host.CreateHostCommand
import org.jwebconsole.server.context.host.DeleteHostCommand
import org.jwebconsole.server.worker.HostWorkerProducerActor
import org.jwebconsole.server.readmodel.hostlist.{SimpleHostViewRequest, SimpleHostViewListRequest}

class HostServlet(override val system: ActorSystem, readModelActor: ActorRef, hostCommandHandler: ActorRef) extends BaseServlet {

  get("/all") {
    executeAsync(readModelActor ? SimpleHostViewListRequest)
  }

  get("/get/:id") {
    val id = params("id")
    executeAsync(readModelActor ? SimpleHostViewRequest(id))
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
