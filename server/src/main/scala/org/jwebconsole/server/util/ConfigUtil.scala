package org.jwebconsole.server.util

import com.typesafe.config.{ConfigValueFactory, ConfigFactory}

object ConfigUtil {

  lazy val config = {
    Option(System.getenv("JWEBCONSOLE_DATA_PATH")).map {
      path =>
        ConfigFactory.load()
          .withValue("akka.persistence.journal.leveldb.dir", ConfigValueFactory.fromAnyRef(path + "/journal"))
          .withValue("akka.persistence.snapshot-store.local.dir", ConfigValueFactory.fromAnyRef(path + "/snapshots"))
          .withValue("h2.connection", ConfigValueFactory.fromAnyRef("jdbc:h2:file:" + path + "/db/db"))
    }.orElse {
      Option(ConfigFactory.load())
    }.get
  }


}
