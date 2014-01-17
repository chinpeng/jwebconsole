package org.jwebconsole.server.servlet

import akka.actor._
import akka.pattern.ask
import org.jwebconsole.server.actor.server.HostManagerActor
import scala.concurrent.Future
import org.jwebconsole.server.model._
import akka.event.LoggingReceive
import org.jwebconsole.server.model.JMXHostStatus
import org.jwebconsole.server.model.HostInfo
import org.jwebconsole.server.model.GetServerStatus
import org.jwebconsole.server.model.MsgWithResponder

class DemoServlet(override val system: ActorSystem) extends DefaultServlet {

  val hostActor = system.actorOf(Props[HostManagerActor])


  before() {
    contentType = formats("json")
  }

  post("/listen") {
    val host = parsedBody.extract[HostInfo]
    val front = system.actorOf(Props(new FrontActor))
    val status: Future[HostConnected] = (front ? ConnectHost(host)).asInstanceOf[Future[HostConnected]]
    executeAsync(status)
  }

  get("/test") {
    <p>Hello</p>
  }

  get("/status/:host/:port") {
    val host = HostInfo(params("host"), params("port").toInt)
    val front = system.actorOf(Props(new FrontActor))
    val status: Future[JMXHostStatus] = (front ? GetServerStatus(host)).asInstanceOf[Future[JMXHostStatus]]
    executeAsync(status)
  }

  class FrontActor extends Actor with ActorLogging {

    var pending: ActorRef = null

    def receive: Receive = {
      case msg: ResponseMessage =>
        pending ! msg
      case message => {
        pending = sender
        system.eventStream.publish(MsgWithResponder(message, self))
      }
    }
  }

}
