package pl.edu.agh.crawler.workers

import org.openqa.selenium.TimeoutException
import pl.edu.agh.crawler.browser.Browser
import pl.edu.agh.crawler.config.crawlerConfig
import pl.edu.agh.crawler.result.{ScrollAttempt, ScrollResult, Timer}


class ScrollCrawler(val browser: Browser) {

  def execute(attempts: Int): ScrollResult = {
    val timeTask = new Timer measure scrollAndCountSuccessfulAttempts(attempts, 0)
    new ScrollResult(timeTask.result.successfulTimes, timeTask.time)
  }

  private def scrollAndCountSuccessfulAttempts(attemptsLeft: Int, successfulAttempts: Int): ScrollAttempt =
    if (attemptsLeft > 0) tryScroll(attemptsLeft, successfulAttempts)
    else new ScrollAttempt(successfulAttempts)

  private def tryScroll(attemptsLeft: Int, successfulAttempts: Int): ScrollAttempt =
    try {
      scrollAndTryNextAttempts(attemptsLeft, successfulAttempts)
    } catch {
      case e: TimeoutException => new ScrollAttempt(successfulAttempts)
    }

  private def scrollAndTryNextAttempts(attemptsLeft: Int, successfulAttempts: Int): ScrollAttempt = {
    performScrolling
    scrollAndCountSuccessfulAttempts(attemptsLeft - 1, successfulAttempts + 1)
  }

  private def performScrolling {
    val height = browser.documentHeight
    browser.scrollToBottom
    waitForResults(height)
  }

  private def waitForResults(height: Long) {
    browser.waitUntil ajaxCompleted (crawlerConfig.scrollEffectAjaxTimeout)
    browser.waitUntil heightExtend (height)
  }
}
