package org.jwebconsole.server

package object jmx {

  case class HeapMemoryUsage(committed: Long = 0, init: Long = 0, max: Long = 0, used: Long = 0)

}
