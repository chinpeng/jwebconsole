package org.jwebconsole.server.actor.memory

import akka.actor.{Props, ActorSystem, Actor}
import java.util.concurrent.Executor
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import org.jwebconsole.server.jmx.{HeapMemoryUsage, JMXWrapper}

object PollerTest extends App {
  val system = ActorSystem("pollers")
  val wrapper = JMXWrapper("localhost", 9004)
  val poller = system.actorOf(Props(new MemoryPollerActor(wrapper)))
  poller ! StartPolling(1 second)
}

class MemoryPollerActor(val wrapper: JMXWrapper) extends Actor {

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
      context.system.eventStream.publish(usage)
  }

  def completeFuture(): Unit = {
    val heapUsage: Future[HeapMemoryUsage] = Future(wrapper.heapUsage)
    heapUsage.onComplete {
      case Success(usage) => self ! ResultReceived(usage)
      case Failure(e) => println("failed")
    }
  }
}

case object PollMemory

case class ResultReceived(usage: HeapMemoryUsage)

case class StartPolling(interval: FiniteDuration)
