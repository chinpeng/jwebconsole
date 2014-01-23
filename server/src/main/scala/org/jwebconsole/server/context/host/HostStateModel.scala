package org.jwebconsole.server.context.host

case class HostStateModel(id: Long = 0L,
                          name: String = "",
                          port: Int = 0,
                          user: String = "",
                          password: String = "",
                          deleted: Boolean = false) {

  def on(event: Any): HostStateModel = event match {
    case HostCreatedEvent(newId, newName, newPort, newUser, newPassword) =>
      copy(id = newId, name = newName, port = newPort, user = newUser, password = newPassword)
    case HostParametersChangedEvent(newId, newName, newPort, newUser, newPassword) =>
      copy(id = newId, name = newName, port = newPort, user = newUser, password = newPassword)
    case HostDeletedEvent(_) =>
      copy(deleted = true)
    case _ => this
  }

}
