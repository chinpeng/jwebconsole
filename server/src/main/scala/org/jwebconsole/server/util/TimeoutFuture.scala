package org.jwebconsole.server.util

import scala.concurrent.duration.FiniteDuration
import scala.concurrent._
import java.util.concurrent.TimeoutException

object TimeoutFuture {

  def apply[T](duration: FiniteDuration, body: =>T)(implicit executionContext: ExecutionContextExecutor) : Future[T] = {
    val promise: Promise[T] = Promise()
    Future(Thread.sleep(duration.toMillis)).onComplete {
      case _ => promise.failure(new TimeoutException())
    }
    promise.completeWith(Future(body))
    promise.future
  }

}
