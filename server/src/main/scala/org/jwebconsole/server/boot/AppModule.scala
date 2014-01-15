package org.jwebconsole.server.boot

import org.eclipse.jetty.servlet.ServletContextHandler
import akka.actor.ActorSystem

trait AppModule {

  implicit val contextHandler: ServletContextHandler
  implicit val system: ActorSystem

  def register() {

  }



}
