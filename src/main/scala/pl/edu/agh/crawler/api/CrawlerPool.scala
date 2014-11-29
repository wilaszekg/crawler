package pl.edu.agh.crawler.api

import java.util.concurrent.LinkedBlockingQueue

import pl.edu.agh.crawler.Crawler
import pl.edu.agh.crawler.phantom.webDriverFactory

class CrawlerPool(val size: Int) {
  val crawlers: Seq[Crawler] =
    for (i <- 0 until size) yield new Crawler(webDriverFactory.createWebDriver)

  val freeCrawlers = new LinkedBlockingQueue[Crawler]
  crawlers foreach (freeCrawlers.put(_))

  def takeCrawler =
    freeCrawlers.take()


  def releaseCrawler(crawler: Crawler) {
    verifyCanRelease(crawler)
    freeCrawlers.put(crawler)
  }

  private def verifyCanRelease(crawler: Crawler) = {
    verifyOwning(crawler)
    verifyNotFree(crawler)
  }

  private def verifyOwning(crawler: Crawler) =
    if (!(crawlers contains crawler)) {
      throw new IllegalStateException("This crawler does not belong to this pool.")
    }

  private def verifyNotFree(crawler: Crawler) =
    if (freeCrawlers contains crawler) {
      throw new IllegalStateException("This crawler is already released.")
    }
}
