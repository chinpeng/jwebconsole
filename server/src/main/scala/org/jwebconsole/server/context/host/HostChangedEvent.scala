package org.jwebconsole.server.context.host

import org.jwebconsole.server.context.util.AppEvent

trait HostChangedEvent extends AppEvent {
  def id: String
}

case class HostDeletedEvent(id: String) extends HostChangedEvent

case class HostCreatedEvent(id: String,
                            name: String,
                            port: Int,
                            user: String = "",
                            password: String = "") extends HostChangedEvent

case class HostParametersChangedEvent(id: String,
                                      name: String,
                                      port: Int,
                                      user: String = "",
                                      password: String = "") extends HostChangedEvent


