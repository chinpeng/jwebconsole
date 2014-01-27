package org.jwebconsole.server.servlet

import akka.actor._

class DemoServlet(override val system: ActorSystem) extends BaseServlet {


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
