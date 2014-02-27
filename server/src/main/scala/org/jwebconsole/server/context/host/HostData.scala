package org.jwebconsole.server.context.host

import java.util.Date

case class HostData(
                     connected: Boolean = false,
                     time: Date = new Date(),
                     threadData: ThreadData = ThreadData()) {}

case class ThreadData(threadCount: Int = 0,
                      peakThreadCount: Int = 0,
                      availableThreads: List[AvailableThread] = List()) {}

case class AvailableThread(id: Long = 0,
                           name: String = "",
                           state: String = "",
                           totalBlocked: Long = 0,
                           totalWaited: Long = 0,
                           stackTrace: List[String] = List())
