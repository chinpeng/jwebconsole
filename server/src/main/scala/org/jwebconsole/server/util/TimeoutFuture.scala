package org.jwebconsole.server.util

import scala.concurrent.duration.FiniteDuration
import scala.concurrent._
import java.util.concurrent.TimeoutException
import akka.actor.ActorSystem

object TimeoutFuture {

  def apply[T](duration: FiniteDuration, body: => T)(implicit system: ActorSystem): Future[T] = {
    implicit val executor = system.dispatcher
    val promise: Promise[T] = Promise()
    val future = Future(body)
    system.scheduler.scheduleOnce(duration) {
      promise.tryFailure(new TimeoutException())
    }
    promise.tryCompleteWith(future)
    promise.future
  }

}
