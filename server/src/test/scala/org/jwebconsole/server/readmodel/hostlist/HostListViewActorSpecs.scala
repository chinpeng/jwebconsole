package org.jwebconsole.server.readmodel.hostlist

import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.time.NoTimeConversions
import org.jwebconsole.server.util.AkkaTestkitSupport
import akka.testkit.TestActorRef
import akka.actor.Props
import org.jwebconsole.server.context.host.{HostData, HostDataChangedEvent}

class HostListViewActorSpecs extends SpecificationWithJUnit with Mockito with NoTimeConversions {

  trait mocks extends AkkaTestkitSupport {
    val dao = mock[SimpleHostDAO]
    dao.exists returns true
    val actor: TestActorRef[HostListViewActor] = TestActorRef(Props(new HostListViewActor(dao)))
    val source = actor.underlyingActor
    val hostId = "test-id"
  }

  "Host List View actor" should {
    "not replay Host Data Change Events" in new mocks {
      source.filterFunc(HostDataChangedEvent(hostId, HostData())) must beFalse
    }
  }


}
