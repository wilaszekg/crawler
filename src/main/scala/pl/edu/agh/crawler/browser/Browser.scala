package pl.edu.agh.crawler.browser

import java.util
import java.util.concurrent.TimeUnit

import org.fluentlenium.core.FluentPage
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.{By, OutputType}
import pl.edu.agh.crawler.config.crawlerConfig
import pl.edu.agh.crawler.result.{RemovedNode, ScreenShotResult, TimeTask, Timer}

import scala.collection.JavaConversions._
import scala.util.Try
import scala.util.parsing.json.JSONArray

class Browser(val driver: PhantomJSDriver) extends FluentPage(driver) {

  override def goTo(url: String) = {
    driver.navigate().to(url)
    resetAjaxCounter()
    await().pollingEvery(100, TimeUnit.MILLISECONDS).atMost(crawlerConfig.pageLoadTimeout, TimeUnit.SECONDS).untilPage().isLoaded
  }

  private def resetAjaxCounter() =
    driver.executePhantomJS("this.ajaxCounter = 0;")

  def executeCrawlingScripts = {
    driver.executeScript(scripts.crawler)
  }

  def prepareCustomScripts = {
    driver.executeScript(scripts.util)
    driver.executeScript(scripts.domEvents)
  }

  def preparePhantomWebPage = {
    driver.executePhantomJS(scripts.phantom.domEvents)
    setExcludedResourcesSuffixes(crawlerConfig.excludedResources.toList)
  }

  def setExcludedResourcesSuffixes(suffixes: List[String]) = {
    val suffixesJson: String = JSONArray.apply(suffixes).toString()
    driver.executePhantomJS(s"this.excludeResources = $suffixesJson;")
  }

  def setOnlyResourceToRequest(url: String) = {
    driver.executePhantomJS(s"this.onlyResourceToRequest = '$url';")
  }

  private def resetOnlyResourceToRequest() = {
    driver.executePhantomJS("this.onlyResourceToRequest = null;")
  }

  def waitUntil = new BrowserWait(this)

  /**
   * Cleans browser state to enable further crawling in this browser
   */
  def cleanUp = {
    driver.executeScript("window.onbeforeunload = null;")
    resetOnlyResourceToRequest()
  }

  def removedContent: Seq[RemovedNode] =
    driver.executeScript("return window.removedNodes;")
      .asInstanceOf[util.List[util.List[String]]]
      .map(contentAndXpath => RemovedNode(contentAndXpath(0), contentAndXpath(1)))

  def removedLinks: Set[String] =
    driver.executeScript("return window.removedLinks;")
      .asInstanceOf[util.List[String]].toSet

  def scrollToBottom = {
    for (i <- 0 until 3)
      driver.executeScript("return window.scrollToBottom();")
  }

  def documentHeight =
    driver.executeScript("return document.body.scrollHeight;").asInstanceOf[Long]

  def deleteAllCookies =
    driver.manage.deleteAllCookies

  def makeScreenShot: ScreenShotResult = {
    new Timer measure driver.getScreenshotAs(OutputType.FILE) match {
      case TimeTask(time, file) => ScreenShotResult(file, time)
    }
  }

  def ajaxRequestsCount: Number =
    driver.executePhantomJS("return this.ajaxCounter").asInstanceOf[Number]

  def iFrames(): Map[String, String] = {
    val iFramesCount = Try {
      driver.findElements(By.tagName("iframe")).size()
    }.getOrElse(0)

    val srcToContent: Map[String, String] = (0 to iFramesCount).map(frameId => Try {
      driver.switchTo().defaultContent()
      val frame = driver.switchTo().frame(frameId)
      (frame.getCurrentUrl, frame.getPageSource)
    }).filter(_.isSuccess)
      .map(_.get)
      .toMap

    driver.switchTo().defaultContent()
    srcToContent
  }

}

