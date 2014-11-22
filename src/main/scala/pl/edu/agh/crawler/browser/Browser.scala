package pl.edu.agh.crawler.browser

import java.util
import java.util.concurrent.TimeUnit

import org.fluentlenium.core.FluentPage
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.{Point, TimeoutException}
import pl.edu.agh.crawler.conditions.{HeightExtendCondition, AjaxSilenceCondition, DomSilenceCondition}

import scala.collection.JavaConversions._

class Browser(val driver: PhantomJSDriver) extends FluentPage(driver) {
  /*def getCandidateElements: PageElementResponse = {
    val result = driver.executeScript("return getCandidateElements(arguments[0])", crawlerConfig.tagsToCrawl)
    result.asInstanceOf[String].parseJson.convertTo[PageElementResponse]
  }*/

  def executeCrawlingScripts = {
    driver.executeScript(scripts.crawler)
  }

  override def goTo(url: String) = {
    driver.navigate().to(url)
    await().pollingEvery(100, TimeUnit.MILLISECONDS).atMost(10, TimeUnit.SECONDS).untilPage().isLoaded
  }

  def prepareCustomScripts = {
    driver.executeScript(scripts.util)
    driver.executeScript(scripts.domEvents)

    driver.executePhantomJS(scripts.phantom.domEvents)
  }

  def waitUntilAjaxCompleted(timeout: Int = 5) =
    try {
      await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(timeout, TimeUnit.SECONDS).until(new AjaxSilenceCondition)
    }
    catch {
      case e: TimeoutException => e.printStackTrace()
    }

  def waitUntilDomStable(timeout: Int = 5) =
    try {
      await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(timeout, TimeUnit.SECONDS).until(new DomSilenceCondition)
    }
    catch {
      case e: TimeoutException => e.printStackTrace()
    }

  def waitUntilHeightExtend(initialHeight: Long) =
    await().pollingEvery(200, TimeUnit.MILLISECONDS).atMost(3, TimeUnit.SECONDS).until(new HeightExtendCondition(this, initialHeight))

  def cleanUp =
    driver.executeScript("window.onbeforeunload = null;")

  def getRemovedText: Seq[String] =
    driver.executeScript("return window.removedNodes;").asInstanceOf[util.List[String]].toList

  def getCurrentHeightAndScrollToBottom = {
    val height: Long = driver.executeScript("return window.getCurrentHeightAndScrollToBottom();").asInstanceOf[Long]
    driver.executeScript("return window.getCurrentHeightAndScrollToBottom();")
    driver.executeScript("return window.getCurrentHeightAndScrollToBottom();")
    height
  }

  def documentHeight =
    driver.executeScript("return document.body.scrollHeight;").asInstanceOf[Long]
}
