package org.jwebconsole.server.servlet

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import akka.actor.ActorRef
import scala.concurrent.duration._
import akka.util.Timeout

class DemoServlet(val sample: ActorRef) extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {


  implicit val timeout = Timeout(5 seconds)
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {

  }


}
