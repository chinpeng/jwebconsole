package org.jwebconsole.server.context.host

import org.specs2.mock.Mockito
import org.specs2.mutable.{SpecificationWithJUnit, Specification}
import org.specs2.specification.Before

class HostStateModelSpecs extends SpecificationWithJUnit with Mockito {

  trait HostData extends Before {
    val testId = "100"
    val name = "localhost"
    val port = 8080
    val login = "login"
    val password = "password"

    def before = {}
  }

  "Host state model" should {
    "apply host created event " in new HostData {
      var model = HostStateModel()
      val event = HostCreatedEvent(testId, name, port, login, password)
      model = model.on(event)
      model.name mustEqual name
    }
  }

  "Host state model" should {
    "apply host changed event" in new HostData {
      var model = HostStateModel()
      val event = HostParametersChangedEvent(testId, name, port, login, password)
      model = model.on(event)
      model.name mustEqual name
    }
  }

  "Host state model" should {
    "be deleted at creation" in new HostData {
      val model = HostStateModel()
      model.deleted mustEqual true
    }
  }

  "Host state model" should {
    "be deleted on deletion event" in new HostData {
      var model = HostStateModel()
      model = model.on(HostDeletedEvent(testId))
      model.deleted mustEqual true
    }
  }

  "Host state model" should {
    "be unchanged if event is undefined" in new HostData {
      val model = HostStateModel()
      val changed = model.on(new HostChangedEvent {
        def id: String = testId
      })
      model mustEqual changed
    }
  }

  "Host state model" should {
    "modify data changed event" in new HostData {
      var model = HostStateModel()
      val event = HostCreatedEvent(testId, name, port, login, password)
      model = model.on(new HostDataChangedEvent(testId, HostData(connected = false)))
      model.data.connected must beFalse
    }
  }

}
