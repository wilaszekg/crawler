package pl.edu.agh.crawler.browser

import java.util
import java.util.concurrent.TimeUnit

import org.fluentlenium.core.FluentPage
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.conditions.{AjaxSilenceCondition, DomSilenceCondition}

import scala.collection.JavaConversions

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

  def waitUntilAjaxCompleted =
    try {
      await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(5, TimeUnit.SECONDS).until(new AjaxSilenceCondition)
    }
    catch {
      case e: TimeoutException => e.printStackTrace()
    }

  def waitUntilDomStable =
    try {
      await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(5, TimeUnit.SECONDS).until(new DomSilenceCondition)
    }
    catch {
      case e: TimeoutException => e.printStackTrace()
    }

  def cleanUp =
    driver.executeScript("window.onbeforeunload = null;")

  def getRemovedText: Seq[String] =
    JavaConversions.asScalaBuffer(driver.executeScript("return window.removedNodes;").asInstanceOf[util.List[String]])

}
