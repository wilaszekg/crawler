package pl.edu.agh.crawler.workers

import org.fluentlenium.core.Fluent
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.browser.Browser
import pl.edu.agh.crawler.description._

class Crawler(val driver: PhantomJSDriver) {

  val browser: Browser = new Browser(driver)

  val scrollCrawler: ScrollCrawler = new ScrollCrawler(browser)

  def crawl(task: CrawlingTask) = {
    val timer = new Timer

    authenticateIfRequired(task)
    val loadTask = openPage(task, timer)
    val scrollTask = scrollCrawler execute task.scrollAttempts
    val wholeTask = timer measure breadthFirstCrawling(task.depth)

    finalizeAndGetResult(task, new CrawlingStatistics(loadTask.time, wholeTask.time, scrollTask.time, scrollTask.result))
  }

  private def authenticateIfRequired(task: CrawlingTask) = {
    if (task.authActionSupplier != null) {
      task.authActionSupplier(driver).perform()
    }
  }

  private def finalizeAndGetResult(task: CrawlingTask, crawlingStatistics: CrawlingStatistics): CrawlResult = {
    val result: CrawlResult =
      new CrawlResult(task, new CrawledContent(driver.getPageSource, browser.getRemovedText), crawlingStatistics)

    browser.cleanUp
    result
  }

  private def openPage(task: CrawlingTask, timer: Timer): TimeTask[Fluent] = {
    val loadTask = timer measure browser.goTo(task.url)
    browser.prepareCustomScripts
    loadTask
  }

  private def breadthFirstCrawling(depth: Int) =
    for (i <- 0 until depth)
      executeCrawling

  private def executeCrawling = {
    browser.executeCrawlingScripts
    browser.waitUntil ajaxCompleted()
    browser.waitUntil domStable()
  }

}
