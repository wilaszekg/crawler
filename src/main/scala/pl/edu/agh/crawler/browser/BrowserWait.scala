package pl.edu.agh.crawler.browser

import java.util.concurrent.TimeUnit

import org.openqa.selenium.TimeoutException
import pl.edu.agh.crawler.conditions.{AjaxSilenceCondition, DomSilenceCondition, HeightExtendCondition}
import pl.edu.agh.crawler.config.crawlerConfig

class BrowserWait(val browser: Browser) {

  def ajaxCompleted(timeout: Int = crawlerConfig.ajaxCompleteTimeout): Unit =
    try {
      browser.await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(timeout, TimeUnit.SECONDS).until(new AjaxSilenceCondition)
    } catch {
      case e: TimeoutException => ()
    }

  def domStable(timeout: Int = crawlerConfig.domStableTimeout): Unit =
    try {
      browser.await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(timeout, TimeUnit.SECONDS).until(new DomSilenceCondition)
    } catch {
      case e: TimeoutException => ()
    }

  def heightExtend(initialHeight: Long) =
    browser.await().
      pollingEvery(200, TimeUnit.MILLISECONDS).
      atMost(crawlerConfig.scrollEffectRenderTimeout, TimeUnit.SECONDS).
      until(new HeightExtendCondition(browser, initialHeight))


}
