package org.jwebconsole.server.actor.helper

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import org.jwebconsole.server.model.{WithResponder, ResponseMessage}
import org.jwebconsole.server.actor.provider.ActorProvider

class GlueActor(val provider: ActorProvider) extends Actor with ActorLogging {

  import provider._
  provider.register(this)

  var pending: ActorRef = null

  def receive: Receive = {
    case response: ResponseMessage =>
      pending ! response
    case message => {
      pending = senderRef
      publishEvent(WithResponder(message, selfRef))
      provider.stopSelf()
    }
  }

}

object GlueActor {
  def apply(): Props = {
    Props(new GlueActor(new ActorProvider))
  }
}
