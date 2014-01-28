import akka.actor.{Props, ActorSystem}
import com.mchange.v2.c3p0.ComboPooledDataSource
import javax.servlet.ServletContext
import org.jwebconsole.server.context.host.HostChangedEvent
import org.jwebconsole.server.context.host.model.{HostReadModel, SimpleHostDAO}
import org.jwebconsole.server.context.util.{ValidationWithSender, AppEvent, GlobalEventStore, ValidationActor}
import org.jwebconsole.server.servlet.HostServlet
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
    val validationActor = system.actorOf(Props[ValidationActor])
    val eventStore = system.actorOf(Props(new GlobalEventStore({
      case _ => true
    })))
    val dao = new SimpleHostDAO(db)
    val readModel = system.actorOf(Props(new HostReadModel(dao)))
    system.eventStream.subscribe(eventStore, classOf[HostChangedEvent])
    system.eventStream.subscribe(readModel, classOf[HostChangedEvent])
    system.eventStream.subscribe(validationActor, classOf[ValidationWithSender[Any]])
    context.mount(new HostServlet(system, readModel), "/hosts/*")
  }

}
