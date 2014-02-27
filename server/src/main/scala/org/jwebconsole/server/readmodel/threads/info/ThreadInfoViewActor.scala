package org.jwebconsole.server.readmodel.threads.info

import akka.actor.{Stash, ActorLogging, Actor}
import org.jwebconsole.server.readmodel.threads._
import org.jwebconsole.server.readmodel.common.ReadModelReplayingActor
import org.jwebconsole.server.context.common.AppEvent
import org.jwebconsole.server.context.host.HostDataChangedEvent

class ThreadInfoViewActor(val dao: ThreadInfoDao) extends Actor with ActorLogging with Stash with ReadModelReplayingActor {

  override def receive: Receive = {
    case _ => Unit
  }

  override def persistReplay(ev: AppEvent): Unit = {

  }

  override def afterRecover(): Unit = {
    log.info("Thread Info View recover completed")
  }

  def filterFunc = commonThreadFilterFunc
}
