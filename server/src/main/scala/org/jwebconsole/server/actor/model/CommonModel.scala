package org.jwebconsole.server.actor.model

sealed trait CommonModel

case object StopActor extends CommonModel

case object RecoveryFinished extends CommonModel

case object CheckRecoveryStatus extends CommonModel