package org.jwebconsole.server.worker

import org.jwebconsole.server.context.host.model.SimpleHostView
import akka.actor.{Cancellable, ActorLogging, Actor, ActorRef}
import org.jwebconsole.server.context.host.{ChangeHostDataCommand, HostData, HostParametersChangedEvent}
import org.jwebconsole.server.jmx.{JMXConnection, JMXConnectionFactory}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import scala.concurrent.Future

class HostWorkerActor(@volatile var host: SimpleHostView,
                      commandHandler: ActorRef,
                      connectionFactory: JMXConnectionFactory,
                      val delay: FiniteDuration = 10.seconds) extends Actor with ActorLogging {

  implicit val executor = context.system.dispatcher

  var timer: Option[Cancellable] = None

  @volatile var connection: Try[JMXConnection] = connectionFactory.createConnection(host.name, host.port)

  override def receive: Receive = {
    case StopWork() =>
      timer.map(_.cancel())
      context.stop(self)
    case StartWork() =>
      startPolling()
    case ev: HostParametersChangedEvent =>
      changeHostParams(ev)
    case MakeConnectionPolling =>
      makePolling()
  }

  def startPolling(): Unit = {
    timer = Some(context.system.scheduler.schedule(delay, delay) {
      self ! MakeConnectionPolling
    })
  }

  def makePolling(): Unit = {
    val currentConnection = connection
    val currentHost = host
    Future(for (conn <- currentConnection) yield conn.connected) onComplete {
      t => t.flatten match {
        case Success(v) => commandHandler ! ChangeHostDataCommand(currentHost.id, HostData(connected = v))
        case Failure(e) => commandHandler ! ChangeHostDataCommand(currentHost.id, HostData(connected = false))
      }
    }
  }

  def changeHostParams(ev: HostParametersChangedEvent): Unit = {
    host = host.copy(name = ev.name, port = ev.port)
  }

}

case class StartWork()

case class StopWork()

case object MakeConnectionPolling