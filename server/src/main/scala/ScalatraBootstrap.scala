import akka.actor.{ActorRef, Props, ActorSystem}
import com.mchange.v2.c3p0.ComboPooledDataSource
import javax.servlet.ServletContext
import org.jwebconsole.server.context.host._
import org.jwebconsole.server.context.host.HostDeletedEvent
import org.jwebconsole.server.context.host.HostParametersChangedEvent
import org.jwebconsole.server.context.common.{AppEvent, GlobalEventStore}
import org.jwebconsole.server.jmx.{JMXConnectionFactory, JMXConnectionChecker}
import org.jwebconsole.server.readmodel.hostlist.{SimpleHostDAO, HostListViewActor, AvailableHostsList}
import org.jwebconsole.server.readmodel.threads.{ThreadDataViewActor, ThreadDataDAO}
import org.jwebconsole.server.servlet.{ThreadDataServlet, HostServlet}
import org.jwebconsole.server.worker.HostWorkerProducerActor
import org.scalatra.LifeCycle
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
    val dao = new SimpleHostDAO(db)
    val connectionChecker = new JMXConnectionChecker()
    val validator = system.actorOf(Props(new HostCommandValidator(connectionChecker)))
    val hostCommandHandler = system.actorOf(Props(new HostCommandHandler(validator)))
    val hostWorkerProducer = createWorkerProducer(system, hostCommandHandler)
    val threadDataView = initThreadReadModel(system)
    val readModel = system.actorOf(Props(new HostListViewActor(dao)))
    system.eventStream.subscribe(eventStore, classOf[AppEvent])
    system.eventStream.subscribe(readModel, classOf[HostChangedEvent])
    context.mount(new HostServlet(system, readModel, hostCommandHandler), "/hosts/*")
    context.mount(new ThreadDataServlet(system, threadDataView), "/thread/*")
  }

  def initThreadReadModel(system: ActorSystem): ActorRef = {
    val dao = new ThreadDataDAO(db)
    val threadDataView = system.actorOf(Props(new ThreadDataViewActor(dao)))
    system.eventStream.subscribe(threadDataView, classOf[HostDataChangedEvent])
    threadDataView
  }

  def createWorkerProducer(system: ActorSystem, hostCommandHandler: ActorRef): ActorRef = {
    val hostWorkerProducer = system.actorOf(Props(new HostWorkerProducerActor(hostCommandHandler, new JMXConnectionFactory())))
    system.eventStream.subscribe(hostWorkerProducer, classOf[AvailableHostsList])
    system.eventStream.subscribe(hostWorkerProducer, classOf[HostParametersChangedEvent])
    system.eventStream.subscribe(hostWorkerProducer, classOf[HostDeletedEvent])
    system.eventStream.subscribe(hostWorkerProducer, classOf[HostCreatedEvent])
    hostWorkerProducer
  }

}
