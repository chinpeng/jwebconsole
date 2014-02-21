package org.jwebconsole.server.context.host

import java.util.Date

case class HostData(
                     connected: Boolean = false,
                     time: Date = new Date(),
                     threadData: ThreadData = ThreadData()) {}

case class ThreadData(threadCount: Int = 0, peakThreadCount: Int = 0) {}
