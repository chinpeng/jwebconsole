package org.jwebconsole.server.util

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.ActorSystem
import org.specs2.mutable.After

abstract class AkkaTestkitSupport extends TestKit(ActorSystem()) with After with ImplicitSender {
  def after = system.shutdown()
}
