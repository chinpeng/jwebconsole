package org.jwebconsole.server.util

import scala.concurrent.duration._

object AppConstants {

  lazy val DefaultTimeout = 4.seconds
  lazy val GlobalEventStoreProcessorId = "global-event-store"
  lazy val HostWorkerPollingInterval = 20.seconds

}
