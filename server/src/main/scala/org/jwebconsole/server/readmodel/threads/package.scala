package org.jwebconsole.server.readmodel

import org.jwebconsole.server.context.common.AppEvent
import org.jwebconsole.server.context.host.{HostDataChangedEvent, HostDeletedEvent}

package object threads {

  def commonThreadFilterFunc: PartialFunction[AppEvent, Boolean] = {
    case ev: HostDeletedEvent => true
    case ev: HostDataChangedEvent => true
    case _ => false
  }

}
