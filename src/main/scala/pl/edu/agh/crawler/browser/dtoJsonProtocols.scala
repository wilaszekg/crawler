package pl.edu.agh.crawler.browser

import spray.json.DefaultJsonProtocol

object dtoJsonProtocols extends DefaultJsonProtocol {
  implicit val pageElementPositionProtocol = jsonFormat6(PageElementPosition)
  implicit val pageElementProtocol = jsonFormat3(PageElement)
  implicit val pageElementResponseProtocol = jsonFormat2(PageElementResponse)
}
