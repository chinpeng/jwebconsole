package org.jwebconsole.server.context.host

import akka.persistence.EventsourcedProcessor

class HostARActor(override val processorId: String) extends EventsourcedProcessor {

  val model = HostStateModel()

  def receiveReplay: Receive = {

  }

  def receiveCommand: Receive = {
    case cmd : CreateHostCommand =>

  }
}
