package pl.edu.agh.crawler.api

import pl.edu.agh.crawler.Crawler
import pl.edu.agh.crawler.description.{CrawlResult, CrawlingTask}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{Promise, future, promise}

class MultiCrawler(val crawlerPool: CrawlerPool) {

  def crawl(crawlTask: CrawlingTask): Promise[CrawlResult] = {
    val resultPromise = promise[CrawlResult]
    dispatchFutureTask(crawlTask, resultPromise, crawlerPool.takeCrawler)
    resultPromise
  }

  private def dispatchFutureTask(crawlTask: CrawlingTask, resultPromise: Promise[CrawlResult], crawler: Crawler) = future {
    try {
      val result: CrawlResult = crawler crawl crawlTask
      resultPromise success result
    }
    catch {
      case e: Exception => resultPromise failure e
    }
    crawler.cleanBrowser
    crawlerPool releaseCrawler crawler
  }
}
