package org.jwebconsole.server.readmodel.common

import akka.actor.{Props, Stash, ActorLogging, Actor}
import org.jwebconsole.server.context.common.{ResponseMessage, ReplayFinished, EventStoreReplayingActor, AppEvent}
import scala.util.{Failure, Success, Try}
import scala.concurrent.Future
import org.jwebconsole.server.util.{ErrorMessage, ErrorMessages}

trait ReadModelActor extends Actor with ActorLogging with Stash {

  implicit val executor = context.system.dispatcher

  def dao: ReplayingDao

  override def preStart() {
    Try(tryRecover()) match {
      case Success(_) =>
        log.debug("Successful recover")
      case Failure(e) =>
        log.error(e, "Unable to recover")
        context.unbecome()
    }
  }

  private def tryRecover(): Unit = {
    if (!dao.exists()) {
      dao.createTable()
      context.become(replayingState)
      makeReplay()
    } else {
      afterRecover()
    }
  }

  private val replayingState: Receive = {
    case ev: AppEvent =>
      tryPersistEvent(ev)
    case ReplayFinished =>
      finishReplay()
    case other =>
      log.debug("Stashing event" + other)
      stash()
  }

  override def receive: Receive = {
    case ev: AppEvent =>
      persistEvent.applyOrElse(ev, handleUnknownEvent)
    case req: ReadModelRequest =>
      if (processRequest.isDefinedAt(req)) makeAsyncResponse(req)
      else processUnhandledRequest(req)
    case other =>
      log.warning("Received unknown message: " + other)
  }

  private def finishReplay(): Unit = {
    context.unbecome()
    unstashAll()
    afterRecover()
    log.debug("Finished replaying state, back to normal")
  }

  private def tryPersistEvent(ev: AppEvent) = Try(persistEvent(ev)) match {
    case Success(_) => log.debug("Persisted replaying event:" + ev)
    case Failure(ex) => log.error(ex, "Unable to persist event: " + ev)
  }

  private def handleUnknownEvent(event: AppEvent): Unit = {
    log.warning("Received unknown event on persisting event: " + event)
  }

  private def makeReplay(): Unit = context.actorOf(Props(new EventStoreReplayingActor(filterFunc, self)))


  private def filterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev => persistEvent.isDefinedAt(ev)
  }

  private def processUnhandledRequest(request: ReadModelRequest): Unit = {
    sender() ! ResponseMessage(error = Some(ErrorMessages.UnknownRequestMessage))
  }

  private def makeAsyncResponse(request: ReadModelRequest): Unit = {
    val currentSender = sender()
    Future(processRequest(request)) onComplete {
      case Success(v) =>
        currentSender ! ResponseMessage(body = Some(v))
      case Failure(e) =>
        handleFailure(request, e)
    }
  }

  def afterRecover(): Unit = Unit

  def persistEvent: PartialFunction[AppEvent, Unit]

  def processRequest: PartialFunction[ReadModelRequest, Any]

  def handleFailure: PartialFunction[(ReadModelRequest, Throwable), ErrorMessage] = {
    case (req, err) => ErrorMessages.UnknownErrorMessage
  }

}
