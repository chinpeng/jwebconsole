package org.jwebconsole.server.context.host

trait HostChangedEvent {
  def id: Long
}

case class HostDeletedEvent(id: Long) extends HostChangedEvent

case class HostCreatedEvent(id: Long,
                            name: String,
                            port: Int,
                            user: String,
                            password: String) extends HostChangedEvent

case class HostParametersChangedEvent(id: Long,
                                      name: String,
                                      port: Int,
                                      user: String,
                                      password: String) extends HostChangedEvent


