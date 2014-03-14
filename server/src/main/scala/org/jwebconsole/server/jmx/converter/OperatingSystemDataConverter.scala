package org.jwebconsole.server.jmx.converter

import org.jwebconsole.server.jmx.JMXConnectionUtil
import javax.management.remote.JMXConnector
import org.jwebconsole.server.context.host.{OperatingSystemData, HostData}

/**
 * Created by amednikov
 * Date: 28.02.14
 * Time: 17:29
 */
class OperatingSystemDataConverter (private val utils: JMXConnectionUtil = new JMXConnectionUtil()) extends JMXDataConverter {
  def fromConnection(connection: JMXConnector, hostData: HostData): HostData = {
    val operatingSystemBean = utils.getOperatingSystemBean(connection)
    val objName = "java.lang:type=OperatingSystem"
    hostData.copy(osData = OperatingSystemData(architecture = operatingSystemBean.getArch,
                                               availableProcessors = operatingSystemBean.getAvailableProcessors,
                                               systemLoadAverage = operatingSystemBean.getSystemLoadAverage,
                                               name = operatingSystemBean.getName,
                                               version = operatingSystemBean.getVersion,
                                               processCPUTime = utils.getObjectAttribute(objName, "ProcessCpuTime", connection).asInstanceOf[Long],
                                               committedVirtualMemorySize = utils.getObjectAttribute(objName, "CommittedVirtualMemorySize", connection).asInstanceOf[Long],
                                               totalPhysicalMemorySize = utils.getObjectAttribute(objName, "TotalPhysicalMemorySize", connection).asInstanceOf[Long],
                                               freePhysicalMemorySize = utils.getObjectAttribute(objName, "FreePhysicalMemorySize", connection).asInstanceOf[Long],
                                               totalSwapSpaceSize = utils.getObjectAttribute(objName, "TotalSwapSpaceSize", connection).asInstanceOf[Long],
                                               freeSwapSpaceSize = utils.getObjectAttribute(objName, "FreeSwapSpaceSize", connection).asInstanceOf[Long]))
  }
}
