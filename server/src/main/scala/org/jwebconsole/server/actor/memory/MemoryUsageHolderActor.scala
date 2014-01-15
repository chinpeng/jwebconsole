package org.jwebconsole.server.actor.memory

import akka.persistence.EventsourcedProcessor
import org.jwebconsole.server.jmx.HeapMemoryUsage
import scala.collection.mutable.ListBuffer

class MemoryUsageHolderActor extends EventsourcedProcessor {

  context.system.eventStream.subscribe(self, classOf[HeapMemoryUsage])

  var usages = ListBuffer.empty[HeapMemoryUsage]

  def receiveReplay: MemoryUsageHolderActor#Receive = {
    case usage: HeapMemoryUsage =>
      usages += usage
  }

  def receiveCommand: MemoryUsageHolderActor#Receive = {
    case usage: HeapMemoryUsage =>
      persist(usage) {
        item =>
          usages += usage
      }
    case GetMemory =>
      sender ! usages.toList
  }
}
