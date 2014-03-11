import akka.actor.{ActorRef, Props, ActorSystem}
import com.mchange.v2.c3p0.ComboPooledDataSource
import javax.servlet.ServletContext
import org.jwebconsole.server.context.host._
import org.jwebconsole.server.context.host.HostDeletedEvent
import org.jwebconsole.server.context.host.HostParametersChangedEvent
import org.jwebconsole.server.context.common.{AppEvent, GlobalEventStore}
import org.jwebconsole.server.jmx.{JMXConnectionFactory, JMXConnectionChecker}
import org.jwebconsole.server.readmodel.hostlist.{SimpleHostDao, HostListViewActor, AvailableHostsList}
import org.jwebconsole.server.readmodel.summary.os.{OperatingSystemViewActor, OperatingSystemDao}
import org.jwebconsole.server.readmodel.threads.count.{ThreadCountViewActor, ThreadCountDao}
import org.jwebconsole.server.readmodel.threads.details.{ThreadDetailsViewActor, ThreadDetailsDao}
import org.jwebconsole.server.readmodel.threads.info.{ThreadInfoViewActor, ThreadInfoDao}
import org.jwebconsole.server.servlet.HostServlet
import org.jwebconsole.server.servlet.summary.OperatingSystemServlet
import org.jwebconsole.server.servlet.thread.{ThreadDetailsServlet, ThreadInfoServlet, ThreadCountServlet}
import org.jwebconsole.server.worker.HostWorkerProducerActor
import org.scalatra.{Handler, LifeCycle}
import scala.slick.driver.H2Driver.simple._

class ScalatraBootstrap extends LifeCycle {

  val db = {
    val ds = new ComboPooledDataSource
    ds.setDriverClass("org.h2.Driver")
    ds.setJdbcUrl("jdbc:h2:mem:test1")
    Database.forDataSource(ds)
  }

  override def init(context: ServletContext) {
    val system = ActorSystem("app-system")
    val eventStore = system.actorOf(Props(new GlobalEventStore))
    val dao = new SimpleHostDao(db)
    val connectionChecker = new JMXConnectionChecker()
    val validator = system.actorOf(Props(new HostCommandValidator(connectionChecker)))
    val hostCommandHandler = system.actorOf(Props(new HostCommandHandler(validator)))
    val hostWorkerProducer = createWorkerProducer(system, hostCommandHandler)
    val threadDataView = initThreadReadModel(system)
    val readModel = system.actorOf(Props(new HostListViewActor(dao)))
    system.eventStream.subscribe(eventStore, classOf[AppEvent])
    system.eventStream.subscribe(readModel, classOf[HostChangedEvent])
    context.mount(new HostServlet(system, readModel, hostCommandHandler), "/hosts/*")
    context.mount(new ThreadCountServlet(system, threadDataView), "/thread/count/*")
    context.mount(createThreadInfoServlet(system), "/thread/info/*")
    context.mount(createOperatingSystemInfoServlet(system), "/summary/*")
    context.mount(createThreadDetailsServlet(system), "/thread/details/*")
  }

  private def initThreadReadModel(system: ActorSystem): ActorRef = {
    val dao = new ThreadCountDao(db)
    val threadDataView = system.actorOf(Props(new ThreadCountViewActor(dao)))
    system.eventStream.subscribe(threadDataView, classOf[HostDataChangedEvent])
    threadDataView
  }

  private def createWorkerProducer(system: ActorSystem, hostCommandHandler: ActorRef): ActorRef = {
    val hostWorkerProducer = system.actorOf(Props(new HostWorkerProducerActor(hostCommandHandler, new JMXConnectionFactory())))
    system.eventStream.subscribe(hostWorkerProducer, classOf[AvailableHostsList])
    system.eventStream.subscribe(hostWorkerProducer, classOf[HostParametersChangedEvent])
    system.eventStream.subscribe(hostWorkerProducer, classOf[HostDeletedEvent])
    system.eventStream.subscribe(hostWorkerProducer, classOf[HostCreatedEvent])
    hostWorkerProducer
  }

  private def createThreadInfoServlet(system: ActorSystem): Handler = {
    val dao = new ThreadInfoDao(db)
    val threadInfoViewActor = system.actorOf(Props(new ThreadInfoViewActor(dao)))
    system.eventStream.subscribe(threadInfoViewActor, classOf[HostDeletedEvent])
    system.eventStream.subscribe(threadInfoViewActor, classOf[HostDataChangedEvent])
    val servlet = new ThreadInfoServlet(system, threadInfoViewActor)
    servlet
  }

  private def createOperatingSystemInfoServlet(system: ActorSystem): Handler = {
    val dao = new OperatingSystemDao(db)
    val operatingSystemViewActor = system.actorOf(Props(new OperatingSystemViewActor(dao)))
    system.eventStream.subscribe(operatingSystemViewActor, classOf[HostDataChangedEvent])
    system.eventStream.subscribe(operatingSystemViewActor, classOf[HostDeletedEvent])
    val servlet = new OperatingSystemServlet(system, operatingSystemViewActor)
    servlet
  }

  private def createThreadDetailsServlet(system: ActorSystem):Handler = {
    val dao = new ThreadDetailsDao(db)
    val viewActor = system.actorOf(Props(new ThreadDetailsViewActor(dao)))
    system.eventStream.subscribe(viewActor, classOf[HostDataChangedEvent])
    system.eventStream.subscribe(viewActor, classOf[HostDeletedEvent])
    val servlet = new ThreadDetailsServlet(system, viewActor)
    servlet
  }

}
