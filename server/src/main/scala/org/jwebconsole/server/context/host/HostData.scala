package org.jwebconsole.server.context.host

case class HostData( connected: Boolean = false, threadData: ThreadData = ThreadData()) {  }

case class ThreadData(threadCount: Int = 0, peakThreadCount: Int = 0) {}
