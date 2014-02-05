package org.jwebconsole.server.util


object ValidationConstants {

  val HostEmptyMessage = InvalidMessage(1, "Host name is empty")
  val PortEmptyMessage = InvalidMessage(2, "Port is empty")
  val PortMustBePositive = InvalidMessage(3, "Port number must be positive")
  val BigNumberForPort = InvalidMessage(4, "Big number for port")
  val HostAlreadyCreated = InvalidMessage(5, "Host Already created")
  val HostDeletedMessage = InvalidMessage(6, "Host already deleted")
  val UnableToConnectMessage = InvalidMessage(7, "Unable to connect")
}
