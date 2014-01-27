package org.jwebconsole.server.context.host

import org.jwebconsole.server.context.util.AppEvent

trait HostChangedEvent extends AppEvent {
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


