package org.jwebconsole.server.context.host

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Before

class HostStateModelSpecs extends Specification with Mockito {

  trait HostData extends Before {
    val id = 100
    val name = "localhost"
    val port = 8080
    val login = "login"
    val password = "password"

    def before = {}
  }

  "Host state model" should {
    "apply host created event " in new HostData {
      var model = HostStateModel()
      val event = HostCreatedEvent(id, name, port, login, password)
      model = model.on(event)
      model.name mustEqual name
    }
  }

  "Host state model" should {
    "apply host changed event" in new HostData {
      var model = HostStateModel()
      val event = HostParametersChangedEvent(id, name, port, login, password)
      model = model.on(event)
      model.name mustEqual name
    }
  }

  "Host state model" should {
    "not be deleted at creation" in new HostData {
      val model = HostStateModel()
      model.deleted mustEqual false
    }
  }

  "Host state model" should {
    "be deleted on deletion event" in new HostData {
      var model = HostStateModel()
      model = model.on(HostDeletedEvent(id))
      model.deleted mustEqual true
    }
  }

  "Host state model" should {
    "be unchanged if event is undefined" in new HostData {
      val model = HostStateModel()
      val changed = model.on(new HostChangedEvent {
        def id: Long = id
      })
      model mustEqual changed
    }
  }

}
