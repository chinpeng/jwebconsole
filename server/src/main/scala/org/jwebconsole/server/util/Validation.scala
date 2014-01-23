package org.jwebconsole.server.util

class Validation {



}

case object ValidationSuccess

case class ValidationFailure(errors: List[String])

/*
* for (i <- validateFirst;
*
*
* */