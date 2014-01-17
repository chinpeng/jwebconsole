package org.jwebconsole.server.servlet

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}
import org.scalatra.scalate.ScalateSupport
import org.scalatra.json.JacksonJsonSupport
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import akka.util.Timeout
import akka.actor.ActorSystem

trait DefaultServlet extends ScalatraServlet with ScalateSupport with JacksonJsonSupport with FutureSupport {

  def system: ActorSystem

  implicit val executor = system.dispatcher

  implicit val timeout = Timeout(5 seconds)
  protected implicit val jsonFormats: Formats = DefaultFormats

  def executeAsync[T](future: Future[T]): AsyncResult = {
    new AsyncResult {
      val is = future
    }
  }


}
