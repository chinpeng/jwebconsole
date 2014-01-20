package org.jwebconsole.server.actor.provider

import akka.actor.{ActorRef, Actor}
import scala.concurrent.duration._
import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext
import org.jwebconsole.server.actor.model.StopActor

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

  def scheduleOnce(time: FiniteDuration, action: () => Unit) = {
    implicit val exec = actor.context.dispatcher.asInstanceOf[Executor with ExecutionContext]
    actor.context.system.scheduler.scheduleOnce(time)(action)
  }

  def scheduleMessageOnce(time: FiniteDuration, ref: ActorRef, msg: Any) {
    scheduleOnce(time, () => ref ! msg)
  }

}

object ActorProvider {

}
