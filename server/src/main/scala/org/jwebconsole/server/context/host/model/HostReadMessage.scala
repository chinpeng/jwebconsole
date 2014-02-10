package org.jwebconsole.server.context.host.model

trait HostReadMessage

case class SimpleHostView(id: String, name: String, port: Int) extends HostReadMessage

case class AvailableHostsList(hosts: List[SimpleHostView])