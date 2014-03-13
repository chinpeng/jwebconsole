package org.jwebconsole.server.util

object Converters {

  implicit class StringConversions(item: String) {

    def emptyToOption(): Option[String] = {
      if (item == null || item.isEmpty) None
      else Some(item)
    }

  }

}
