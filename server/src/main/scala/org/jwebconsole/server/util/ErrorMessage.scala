package org.jwebconsole.server.util

case class ErrorMessage(id: Int, message: String)

object ErrorMessages {

  val UnknownErrorMessage = ErrorMessage(1, "Unknown Error")
  val DbConnectionFailureMessage = ErrorMessage(2, "Unable to connect to database")
  val HostNotFoundMessage = ErrorMessage(3, "Host not found")

}
