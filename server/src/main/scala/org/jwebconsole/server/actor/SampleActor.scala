package org.jwebconsole.server.actor

import akka.actor.Actor

class SampleActor extends Actor {

  def receive: Receive = {
    case Check =>
      sender ! Item(1, "first") :: Item(2, "second") :: Nil
  }

}

case class Item(id: Int, name: String)
case object Check
