package pl.edu.agh.crawler.browser

import java.util.concurrent.TimeUnit

import org.openqa.selenium.TimeoutException
import pl.edu.agh.crawler.conditions.{AjaxSilenceCondition, DomSilenceCondition, HeightExtendCondition}

class BrowserWait(val browser: Browser) {

  def ajaxCompleted(timeout: Int = 5) =
    try {
      browser.await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(timeout, TimeUnit.SECONDS).until(new AjaxSilenceCondition)
    }
    catch {
      case e: TimeoutException => e.printStackTrace()
    }

  def domStable(timeout: Int = 5) =
    try {
      browser.await().pollingEvery(500, TimeUnit.MILLISECONDS).atMost(timeout, TimeUnit.SECONDS).until(new DomSilenceCondition)
    }
    catch {
      case e: TimeoutException => e.printStackTrace()
    }

  def heightExtend(initialHeight: Long) =
    browser.await().pollingEvery(200, TimeUnit.MILLISECONDS).atMost(3, TimeUnit.SECONDS).until(new HeightExtendCondition(browser, initialHeight))


}
