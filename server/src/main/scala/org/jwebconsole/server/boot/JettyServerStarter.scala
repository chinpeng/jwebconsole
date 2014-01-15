package org.jwebconsole.server.boot

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.jwebconsole.server.servlet.DemoServlet
import akka.actor.{Props, ActorSystem}
import org.jwebconsole.server.util.AppConstants
import org.jwebconsole.server.jmx.JMXWrapper
import org.jwebconsole.server.actor.memory.{StartPolling, MemoryPollerActor}
import scala.concurrent.duration._


object JettyServerStarter extends App {

  val server = new Server(8080)
  val contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)
  contextHandler.setContextPath("/")
  contextHandler.setAttribute(AppConstants.ActorSystemContextAttribute, createActorSystem())
  server.setHandler(contextHandler)
  contextHandler.addServlet(classOf[DemoServlet], "/")
  server.start()

  def createActorSystem(): ActorSystem = {
    val system = ActorSystem("jwebconsole-system")
    val jmx = JMXWrapper("localhost", 8888)
    val poller = system.actorOf(Props(new MemoryPollerActor(jmx)))
    poller ! StartPolling(10 seconds)
    system
  }

}
