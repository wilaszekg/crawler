package pl.edu.agh.crawler

import java.io.{File, PrintWriter}

import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.browser.Browser

class Crawler(val driver: PhantomJSDriver) {

  val browser: Browser = new Browser(driver)
  var busy = false

  def crawl(task: CrawlingTask) = {
    val start = System.currentTimeMillis()
    browser.goTo(task.url)

    val loadTime = System.currentTimeMillis() - start

    browser.prepareCustomScripts
    browser.executeCrawlingScripts

    browser.waitUntilAjaxCompleted
    browser.waitUntilDomStable

    val crawlTime = System.currentTimeMillis() - start

    new CrawlResult(task,
      driver.getPageSource,
      browser.getRemovedText,
      new CrawlingStatistics(loadTime, crawlTime))
  }

  def cleanBrowser = browser.cleanUp


}
