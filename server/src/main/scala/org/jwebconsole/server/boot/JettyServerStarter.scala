package org.jwebconsole.server.boot

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.jwebconsole.server.servlet.DemoServlet
import akka.actor.ActorSystem


object JettyServerStarter extends App {
  val server = new Server(8080)
  val contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)
  contextHandler.setContextPath("/")
  contextHandler.setAttribute("actorSystem", createActorSystem())
  server.setHandler(contextHandler)
  contextHandler.addServlet(classOf[DemoServlet], "/")
  server.start()

  def createActorSystem(): ActorSystem = ActorSystem("jwebconsole-system")

}
