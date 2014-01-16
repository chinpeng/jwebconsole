package org.jwebconsole.server.actor.poller

import akka.actor.{Props, ActorSystem, Actor}
import java.util.concurrent.Executor
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Try, Failure, Success}
import org.jwebconsole.server.jmx.{HeapMemoryUsage, JMXWrapper}

class JMXPollerActor(val wrapper: JMXWrapper) extends Actor {

  implicit val exec = context.dispatcher.asInstanceOf[Executor with ExecutionContext]

  def startPolling(duration: FiniteDuration): Unit = {
    context.system.scheduler.schedule(0 seconds, duration) {
      self ! PollMemory
    }
  }

  def receive: Actor.Receive = {
    case StartPolling(duration) =>
      startPolling(duration)
    case PollMemory =>
      completeFuture()
    case ResultReceived(usage) =>
      println(usage)
  }

  def completeFuture(): Unit = {
    val heapUsage: Future[Try[HeapMemoryUsage]] = Future(wrapper.heapUsage)
    heapUsage.onSuccess {
      case Success(usage) => self ! ResultReceived(usage)
      case Failure(e) => println("failed")
    }
  }
}

case object PollMemory

case class ResultReceived(usage: HeapMemoryUsage)

case class StartPolling(interval: FiniteDuration)
