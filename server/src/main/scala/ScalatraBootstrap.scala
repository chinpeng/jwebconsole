import akka.actor.ActorSystem
import javax.servlet.ServletContext
import org.jwebconsole.server.servlet.DemoServlet
import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {

  override def init(context: ServletContext) {
    val system = ActorSystem("app-system")
    context.mount(new DemoServlet(system), "/hosts/*")
  }

}
