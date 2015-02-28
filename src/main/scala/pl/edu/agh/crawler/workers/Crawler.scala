package pl.edu.agh.crawler.workers

import org.fluentlenium.core.Fluent
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.action.ActionSupplier
import pl.edu.agh.crawler.browser.Browser
import pl.edu.agh.crawler.result._
import pl.edu.agh.crawler.task.{ComposedAuthTask, ComposedTask, CrawlTask, SingleTask}

class Crawler(val browser: Browser, val driver: PhantomJSDriver) {

  val scrollCrawler: ScrollCrawler = new ScrollCrawler(browser)
  browser.preparePhantomWebPage

  def this(driver: PhantomJSDriver) = {
    this(new Browser(driver), driver)
  }

  def crawl(task: CrawlTask): CrawlResult = {
    task match {
      case ComposedAuthTask(composedTask, authAction) => crawlWithAuthentication(composedTask, authAction)
      case ComposedTask(tasks, clearCookies) => crawlMany(tasks, clearCookies)
      case singleTask: SingleTask => crawlSingle(singleTask)
    }
  }

  private def crawlWithAuthentication(composedTask: ComposedTask, authAction: ActionSupplier) = {
    authAction(driver).perform()
    crawlMany(composedTask.tasks, composedTask.clearCookies)
  }

  private def crawlMany(tasks: List[SingleTask], clearCookies: Boolean): CollectedResult = {
    val crawledTasks = tasks.map(crawlSingle)
    if (clearCookies) {
      browser.deleteAllCookies
    }
    CollectedResult(crawledTasks)
  }

  private def crawlSingle(task: SingleTask) = {
    try tryCrawl(task)
    catch {
      case cause: Throwable => CrawlFail(task, cause)
    }
  }

  def tryCrawl(task: SingleTask): SingleResult = {
    val timer = new Timer

    val loadTask = openPage(task, timer)
    val scrollTask = scrollCrawler execute task.scrollAttempts
    val wholeTask = timer measure breadthFirstCrawling(task.depth)

    finalizeAndGetResult(task, new CrawlingStatistics(loadTask.time, wholeTask.time, scrollTask.time, scrollTask.result))
  }

  private def finalizeAndGetResult(task: SingleTask, crawlingStatistics: CrawlingStatistics): SingleResult = {
    val result: SingleResult =
      SingleResult(task, new CrawledContent(driver.getPageSource, browser.getRemovedText), crawlingStatistics)

    browser.cleanUp
    result
  }

  private def openPage(task: SingleTask, timer: Timer): TimeTask[Fluent] = {
    if (task.getPageSourceOnly) {
      browser.setOnlyResourceToRequest(task.url)
    }
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
