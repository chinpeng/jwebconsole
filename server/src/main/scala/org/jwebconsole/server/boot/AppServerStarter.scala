package org.jwebconsole.server.boot

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.scalatra.servlet.ScalatraListener

object AppServerStarter extends App {

  val server = new Server(8080)
  val contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS)
  contextHandler.setContextPath("/")
  contextHandler.addEventListener(new ScalatraListener)
  server.setHandler(contextHandler)
  server.start()
}
