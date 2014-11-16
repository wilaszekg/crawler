package pl.edu.agh.crawler.browser

import java.util.concurrent.TimeUnit

import com.google.common.base.Predicate
import org.fluentlenium.core.{Fluent, FluentPage}
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.browser.dtoJsonProtocols._
import pl.edu.agh.crawler.conditions.AjaxSilenceCondition
import pl.edu.agh.crawler.config.crawlerConfig
import spray.json._

class Browser(val driver: PhantomJSDriver) extends FluentPage(driver) {
  /*def getCandidateElements: PageElementResponse = {
    val result = driver.executeScript("return getCandidateElements(arguments[0])", crawlerConfig.tagsToCrawl)
    result.asInstanceOf[String].parseJson.convertTo[PageElementResponse]
  }*/

  def executeCrawlingScripts = {
    driver.executeScript(scripts.crawler)
  }

  override def goTo(url: String) = {
    super.goTo(url)
    await().pollingEvery(1000, TimeUnit.MILLISECONDS).atMost(10, TimeUnit.SECONDS).untilPage().isLoaded
  }

  def prepareCustomScripts = {
    driver.executeScript(scripts.util)
    driver.executeScript(scripts.domEvents)

    driver.executePhantomJS(scripts.phantom.domEvents)
    Thread.sleep(200)
  }

  def waitUntilAjaxCompleted = {
    try {
      await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(5, TimeUnit.SECONDS).until(new AjaxSilenceCondition)
    }
    catch {
      case e: TimeoutException => e.printStackTrace()
    }
  }

  def getRemovedText =
    driver.executeScript("return window.removed_nodes;")
}
