package pl.edu.agh.crawler.browser

import java.util
import java.util.concurrent.TimeUnit

import org.fluentlenium.core.FluentPage
import org.openqa.selenium.phantomjs.PhantomJSDriver

import scala.collection.JavaConversions._

class Browser(val driver: PhantomJSDriver) extends FluentPage(driver) {

  override def goTo(url: String) = {
    driver.navigate().to(url)
    await().pollingEvery(100, TimeUnit.MILLISECONDS).atMost(10, TimeUnit.SECONDS).untilPage().isLoaded
  }

  def executeCrawlingScripts = {
    driver.executeScript(scripts.crawler)
  }

  def prepareCustomScripts = {
    driver.executeScript(scripts.util)
    driver.executeScript(scripts.domEvents)

    driver.executePhantomJS(scripts.phantom.domEvents)
  }

  def waitUntil = new BrowserWait(this)

  /**
   * Cleans browser state to enable further crawling in this browser
   */
  def cleanUp =
    driver.executeScript("window.onbeforeunload = null;")

  def getRemovedText: Seq[String] =
    driver.executeScript("return window.removedNodes;").asInstanceOf[util.List[String]].toList

  def scrollToBottom = {
    for(i <- 0 until 3)
      driver.executeScript("return window.scrollToBottom();")
  }

  def documentHeight =
    driver.executeScript("return document.body.scrollHeight;").asInstanceOf[Long]
}
