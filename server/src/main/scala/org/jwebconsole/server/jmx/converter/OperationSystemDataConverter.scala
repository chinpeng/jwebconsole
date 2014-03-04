package org.jwebconsole.server.jmx.converter

import org.jwebconsole.server.jmx.JMXConnectionUtil
import javax.management.remote.JMXConnector
import org.jwebconsole.server.context.host.{OperationSystemData, HostData}

/**
 * Created by amednikov
 * Date: 28.02.14
 * Time: 17:29
 */
class OperationSystemDataConverter (private val utils: JMXConnectionUtil = new JMXConnectionUtil()) extends JMXDataConverter {
  def fromConnection(connection: JMXConnector, hostData: HostData): HostData = {
    val operatingSystemBean = utils.getOperatingSystemBean(connection)
    hostData.copy(osData = OperationSystemData(architecture = operatingSystemBean.getArch,
                                               availableProcessors = operatingSystemBean.getAvailableProcessors,
                                               systemLoadAverage = operatingSystemBean.getSystemLoadAverage,
                                               name = operatingSystemBean.getName,
                                               version = operatingSystemBean.getVersion))
  }
}
