package org.jwebconsole.server.servlet

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

class DemoServlet extends ScalatraServlet with ScalateSupport with JacksonJsonSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    Item(1, "first") :: Item(2, "second") :: Nil
  }

  case class Item(id: Int, name: String) {

  }

}
