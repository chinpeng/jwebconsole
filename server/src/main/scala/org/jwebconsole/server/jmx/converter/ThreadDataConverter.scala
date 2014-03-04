package org.jwebconsole.server.jmx.converter

import org.jwebconsole.server.context.host.{AvailableThread, HostData, ThreadData}
import javax.management.remote.JMXConnector
import java.lang.management.ThreadMXBean
import org.jwebconsole.server.jmx.JMXConnectionUtil

class ThreadDataConverter(private val utils: JMXConnectionUtil = new JMXConnectionUtil()) extends JMXDataConverter {

  def fromConnection(connection: JMXConnector, hostData: HostData): HostData = {
    val threadBean = utils.getThreadBean(connection)
    val threadCount = threadBean.getThreadCount
    val peakThreadCount = threadBean.getPeakThreadCount
    val threads = availableThreads(threadBean)
    hostData.copy(threadData = ThreadData(threadCount, peakThreadCount, threads))
  }

  private def availableThreads(threadBean: ThreadMXBean): List[AvailableThread] = {
    val ids = threadBean.getAllThreadIds
    threadBean.getThreadInfo(ids).toList.filter(_ != null).map {
      info =>
        val id = info.getThreadId
        val name = info.getThreadName
        val state = info.getThreadState.name()
        val totalBlocked = info.getBlockedCount
        val totalWaited = info.getWaitedCount
        val stackTrace = info.getStackTrace.map(_.toString).toList
        AvailableThread(id, name, state, totalBlocked, totalWaited, stackTrace)
    }
  }


}
