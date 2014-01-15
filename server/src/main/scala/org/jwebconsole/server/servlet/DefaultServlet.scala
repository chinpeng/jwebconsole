package org.jwebconsole.server.servlet

import akka.actor.ActorSystem
import org.jwebconsole.server.util.AppConstants
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}
import org.scalatra.scalate.ScalateSupport
import org.scalatra.json.JacksonJsonSupport
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.duration._
import akka.util.Timeout

trait DefaultServlet extends ScalatraServlet with ScalateSupport with JacksonJsonSupport with FutureSupport {

  implicit val timeout = Timeout(5 seconds)
  protected implicit val jsonFormats: Formats = DefaultFormats

  def system: ActorSystem = {
    servletContext.getAttribute(AppConstants.ActorSystemContextAttribute).asInstanceOf[ActorSystem]
  }

  protected implicit def executor: ExecutionContext = system.dispatcher

  def executeAsync[T](future: Future[T]): AsyncResult = {
    new AsyncResult {
      val is = future
    }
  }


}
