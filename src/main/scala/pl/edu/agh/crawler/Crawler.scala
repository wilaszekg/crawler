package pl.edu.agh.crawler

import org.openqa.selenium.TimeoutException
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.browser.Browser

class Crawler(val driver: PhantomJSDriver) {

  val browser: Browser = new Browser(driver)
  var busy = false

  private val maxScrolls = 3

  def crawl(task: CrawlingTask) = {
    val start = System.currentTimeMillis()
    browser.goTo(task.url)

    val loadTime = System.currentTimeMillis() - start

    browser.prepareCustomScripts

    val beforeScroll = System.currentTimeMillis()
    scrollCrawl(maxScrolls)
    val scrollTime = System.currentTimeMillis() - beforeScroll
    println("Scrolling time: " + scrollTime)

    browser.executeCrawlingScripts

    browser.waitUntilAjaxCompleted()
    browser.waitUntilDomStable()

    val crawlTime = System.currentTimeMillis() - start

    new CrawlResult(task,
      driver.getPageSource,
      browser.getRemovedText,
      new CrawlingStatistics(loadTime, crawlTime))
  }

  def cleanBrowser = browser.cleanUp

  private def scrollCrawl(timesToScroll: Int): Unit = {
    println("Attempting to scroll: " + driver.toString)
    val height = browser.getCurrentHeightAndScrollToBottom

    if (timesToScroll > 1)
      try {
        browser.waitUntilAjaxCompleted(3)
        browser.waitUntilHeightExtend(height)
        scrollCrawl(timesToScroll - 1)
      }
      catch {
        case e: TimeoutException => println("No infinite scroll: " + driver.toString)
      }

  }


}
