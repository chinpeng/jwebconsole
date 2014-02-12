package org.jwebconsole.server.context.host

case class HostStateModel(id: String = "",
                          name: String = "",
                          port: Int = 0,
                          user: String = "",
                          password: String = "",
                          deleted: Boolean = true,
                          data: HostData = HostData()) {

  def on(event: Any): HostStateModel = event match {
    case HostCreatedEvent(newId, newName, newPort, newUser, newPassword) =>
      copy(id = newId, name = newName, port = newPort, user = newUser, password = newPassword, deleted = false)
    case HostParametersChangedEvent(newId, newName, newPort, newUser, newPassword) =>
      copy(id = newId, name = newName, port = newPort, user = newUser, password = newPassword)
    case HostDeletedEvent(_) =>
      copy(deleted = true)
    case HostDataChangedEvent(_, hostData) =>
      copy(data = hostData)
    case _ => this
  }

}
