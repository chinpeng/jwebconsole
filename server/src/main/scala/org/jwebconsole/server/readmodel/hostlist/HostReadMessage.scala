package org.jwebconsole.server.readmodel.hostlist

trait HostReadMessage

case class SimpleHostView(id: String, name: String, port: Int, connected: Boolean = false) extends HostReadMessage

case class AvailableHostsList(hosts: List[SimpleHostView])