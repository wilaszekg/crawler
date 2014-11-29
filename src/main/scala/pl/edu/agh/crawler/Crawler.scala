package pl.edu.agh.crawler

import org.openqa.selenium.TimeoutException
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.browser.Browser
import pl.edu.agh.crawler.description._

class Crawler(val driver: PhantomJSDriver) {

  val browser: Browser = new Browser(driver)

  private val maxScrolls = 3

  def crawl(task: CrawlingTask) = {
    val timer = new Timer
    val loadTask = timer measure browser.goTo(task.url)

    browser.prepareCustomScripts

    val scrollTask = new Timer measure scrollCrawl(maxScrolls)

    val wholeTask = timer measure executeCrawling

    new CrawlResult(task,
      driver.getPageSource,
      browser.getRemovedText,
      new CrawlingStatistics(loadTask.time, wholeTask.time, scrollTask.time))
  }

  private def executeCrawling = {
    browser.executeCrawlingScripts
    browser.waitUntil ajaxCompleted()
    browser.waitUntil domStable()
  }

  private def scrollCrawl(timesToScroll: Int): Unit = {
    println("Attempting to scroll: " + driver.toString)
    val height = browser.documentHeight
    browser.scrollToBottom

    if (timesToScroll > 1)
      try {
        browser.waitUntil ajaxCompleted (3)
        browser.waitUntil heightExtend (height)
        scrollCrawl(timesToScroll - 1)
      }
      catch {
        case e: TimeoutException => println("No infinite scroll: " + driver.toString)
      }
  }

  def cleanBrowser = browser.cleanUp

}
