package pl.edu.agh.crawler.environment

import pl.edu.agh.crawler.Crawler
import pl.edu.agh.crawler.phantom.webDriverFactory

import scala.collection.immutable.IndexedSeq

class CrawlerPool(val size: Int) {
  private val crawlers: IndexedSeq[Crawler] =
    for (i <- 0 until size) yield new Crawler(webDriverFactory.createWebDriver)

  def getFreeCrawler = this.synchronized {
    val freeCrawler: Option[Crawler] = crawlers.find(c => !(c busy))
    freeCrawler match {
      case Some(crawler) => crawler.busy = true
        println(">>>>>>>>>>>>>>>>>>>>>>> TAKING: " + crawler)
      case None =>
    }
    freeCrawler
  }

  def releaseCrawler(crawler: Crawler) = this.synchronized {
    crawler.busy = false
    crawler.cleanBrowser
    println(">>>>>>>>>>>>>>>>>>>>>>> RELEASING: " + crawler)
  }
}
