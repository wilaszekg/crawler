package pl.edu.agh.crawler.concurrent

import pl.edu.agh.crawler.result.CrawlingResult
import pl.edu.agh.crawler.task.SingleTask
import pl.edu.agh.crawler.workers.Crawler

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Promise, future, promise}

class MultiCrawler(val crawlerPool: CrawlerPool) {

  def crawl(crawlTask: SingleTask) = {
    val resultPromise = promise[CrawlingResult]
    dispatchFutureTask(crawlTask, resultPromise, crawlerPool.takeCrawler)
    resultPromise
  }

  private def dispatchFutureTask(crawlTask: SingleTask, resultPromise: Promise[CrawlingResult], crawler: Crawler) = future {
    try {
      val result: CrawlingResult = crawler crawl crawlTask
      resultPromise success result
    }
    catch {
      case e: Exception => resultPromise failure e
    }
    crawlerPool releaseCrawler crawler
  }
}
