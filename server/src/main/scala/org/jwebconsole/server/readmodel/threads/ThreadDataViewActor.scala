package org.jwebconsole.server.readmodel.threads

import akka.actor.{Actor, ActorLogging, Stash}
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor
import org.jwebconsole.server.context.common.AppEvent
import org.jwebconsole.server.context.host.HostDataChangedEvent

class ThreadDataViewActor(val dao: ThreadDataDAO) extends Actor with ActorLogging with Stash with ReadModelReplayingActor {

  def filterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostDataChangedEvent => true
    case _ => false
  }

  def afterRecover(): Unit = Unit

  def persistReplay(event: AppEvent): Unit = event match {
    case ev: HostDataChangedEvent =>
      dao.addThreadDataRecord(ev.id, ev.data.threadData)
  }

  def receive: Receive = {
    case ev: HostDataChangedEvent =>

  }



}
