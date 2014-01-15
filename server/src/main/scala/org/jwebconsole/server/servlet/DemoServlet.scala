package org.jwebconsole.server.servlet

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import akka.actor.{Props, ActorRef, ActorSystem}
import org.jwebconsole.server.actor.{Check, SampleActor}
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout

class DemoServlet extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {


  implicit val timeout = Timeout(5 seconds)
  protected implicit val jsonFormats: Formats = DefaultFormats
  var sample:ActorRef = null

  before() {
    contentType = formats("json")
    val actorSystem = getServletContext.getAttribute("actorSystem").asInstanceOf[ActorSystem]
    sample = actorSystem.actorOf(Props[SampleActor])
  }

  get("/") {
    val responseFuture = sample ? Check
    Await.result(responseFuture, 5 seconds)
  }



}
