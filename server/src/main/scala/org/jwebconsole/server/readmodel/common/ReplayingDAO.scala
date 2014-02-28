package org.jwebconsole.server.readmodel.common

import scala.slick.driver.H2Driver.simple._
import java.util.concurrent.locks.ReentrantLock

trait ReplayingDao {

  val lock = new ReentrantLock(true)

  def createTable(): Unit

  def exists(): Boolean

  implicit class DatabaseExtensions(db: Database) {
    def withLockedSession[T](f: Session => T): T = {
      lock.lock()
      try {
        db.withSession(f)
      } finally {
        lock.unlock()
      }
    }
  }


}


