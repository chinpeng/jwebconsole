package org.jwebconsole.server.readmodel.hostlist

trait HostReadMessage

case class SimpleHostView(id: String,
                          name: String,
                          port: Int,
                          login: String = "",
                          password: String = "",
                          connected: Boolean = false) extends HostReadMessage

case class AvailableHostsList(hosts: List[SimpleHostView])