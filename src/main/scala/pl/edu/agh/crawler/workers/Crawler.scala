package pl.edu.agh.crawler.workers

import org.fluentlenium.core.Fluent
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.browser.Browser
import pl.edu.agh.crawler.description._

class Crawler(val driver: PhantomJSDriver) {

  val browser: Browser = new Browser(driver)

  def crawl(task: CrawlingTask) = {
    val timer = new Timer

    val loadTask: TimeTask[Fluent] = openPage(task, timer)
    val scrollTask = new ScrollCrawler(browser, task.scrollAttempts) execute
    val wholeTask = timer measure executeCrawling

    new CrawlResult(task,
      new CrawledContent(driver.getPageSource, browser.getRemovedText),
      new CrawlingStatistics(loadTask.time, wholeTask.time, scrollTask.time, scrollTask.result))
  }

  private def openPage(task: CrawlingTask, timer: Timer): TimeTask[Fluent] = {
    val loadTask = timer measure browser.goTo(task.url)
    browser.prepareCustomScripts
    loadTask
  }

  private def executeCrawling = {
    browser.executeCrawlingScripts
    browser.waitUntil ajaxCompleted()
    browser.waitUntil domStable()
  }

  def cleanBrowser = browser.cleanUp

}
