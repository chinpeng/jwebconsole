package org.jwebconsole.server.actor.provider

import akka.actor.{ActorRef, Actor}

class ActorProvider {

  var actor: Actor = null

  def register(actor: Actor) {
    this.actor = actor
  }

  def publishEvent(event: Any): Unit = {
    actor.context.system.eventStream.publish(event.asInstanceOf[Object])
  }

  def senderRef: ActorRef = actor.context.sender

  def selfRef: ActorRef = actor.self

  def stopSelf() = actor.context.stop(selfRef)

}

object ActorProvider {

}
