package org.jwebconsole.server.actor.helper

import akka.actor.{Props, ActorRef, ActorLogging, Actor}
import org.jwebconsole.server.model.{WithResponder, ResponseMessage}
import org.jwebconsole.server.actor.provider.ActorProvider
import org.jwebconsole.server.util.AppConstants
import org.jwebconsole.server.actor.model.StopActor

class GlueActor(val provider: ActorProvider) extends Actor with ActorLogging {

  import provider._

  provider.register(this)


  var pending: ActorRef = null

  def receive: Receive = {
    case StopActor =>
      stopSelf()
    case response: ResponseMessage =>
      pending ! response
      stopSelf()
    case message => {
      scheduleMessageOnce(AppConstants.DefaultTimeout, selfRef, StopActor)
      pending = senderRef
      publishEvent(WithResponder(message, selfRef))
    }
  }

}

object GlueActor {
  def apply(): Props = {
    Props(new GlueActor(new ActorProvider))
  }
}
