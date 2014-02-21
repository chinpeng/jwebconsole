package org.jwebconsole.server.context.common

import org.jwebconsole.server.util.{ErrorMessage, InvalidMessage}

trait AppEvent

trait CommonMessages

case class ResponseMessage(body: Option[Any] = None, messages: Option[List[InvalidMessage]] = None, error: Option[ErrorMessage] = None)

case object ReplayFinished extends CommonMessages
