package org.jwebconsole.server.servlet

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{CorsSupport, AsyncResult, FutureSupport, ScalatraServlet}
import org.scalatra.scalate.ScalateSupport
import org.scalatra.json.JacksonJsonSupport
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.util.Timeout
import akka.actor.ActorSystem
import java.text.SimpleDateFormat

trait BaseServlet extends ScalatraServlet with ScalateSupport with JacksonJsonSupport with FutureSupport with CorsSupport {

  options("/*") {
    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"))
  }

  def system: ActorSystem

  implicit val executor = system.dispatcher

  implicit val timeout = Timeout(5 seconds)

  implicit val jsonFormats = new DefaultFormats {
    override def dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  }

  def executeAsync[T](future: Future[T]): AsyncResult = {
    val delay = Future(Thread.sleep(1000))
    val result = for (first <- delay;
        second <- future) yield second
    new AsyncResult {
      val is = result
    }
  }

}
