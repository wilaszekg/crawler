package pl.edu.agh.crawler.browser

import scala.io.{BufferedSource, Source}

object scripts {
  val scriptsDirectory = "src/main/resources/javascript/"
  lazy val cssPath = readScript("css_path.js")
  lazy val candidateElements = readScript("candidate_element.js")
  lazy val elementVisibility = readScript("element_visibility.js")
  lazy val util = readScript("util.js")
  lazy val crawler = readScript("crawler.js")
  lazy val domEvents = readScript("dom_events.js")

  object phantom {
    lazy val domEvents = readPhantomScript("dom_events.js")
  }

  private def readPhantomScript(fileName: String) = readScript("phantom/" + fileName)

  private def readScript(fileName: String): String = {
    val source: BufferedSource = Source.fromFile(scriptsDirectory + fileName)
    val script: String = source.mkString
    source.close()
    script
  }

}
