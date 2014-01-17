package org.jwebconsole.server.boot

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{DefaultServlet, ServletHolder, ServletContextHandler}
import org.jwebconsole.server.servlet.DemoServlet
import akka.actor.ActorSystem
import org.scalatra.servlet.ScalatraListener


object JettyServerStarter extends App {
  val server = new Server(8080)
  val contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)
  contextHandler.setContextPath("/")
  contextHandler.addEventListener(new ScalatraListener)
  contextHandler.addServlet(classOf[DefaultServlet], "/")
  server.setHandler(contextHandler)
  server.start()
  server.join()

}
