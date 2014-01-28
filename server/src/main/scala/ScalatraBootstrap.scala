import akka.actor.{Props, ActorSystem}
import javax.servlet.ServletContext
import org.jwebconsole.server.context.host.HostCommandHandler
import org.jwebconsole.server.context.util.{ValidationWithSender, AppEvent, GlobalEventStore, ValidationActor}
import org.jwebconsole.server.servlet.DemoServlet
import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {

  val db = {

  }

  override def init(context: ServletContext) {
    val system = ActorSystem("app-system")
    val validationActor = system.actorOf(Props[ValidationActor])
    val eventStore = system.actorOf(Props(new GlobalEventStore({
      case _ => true
    })))
    system.eventStream.subscribe(eventStore, classOf[AppEvent])
    system.eventStream.subscribe(validationActor, classOf[ValidationWithSender[Any]])
    context.mount(new DemoServlet(system), "/hosts/*")
  }

}
