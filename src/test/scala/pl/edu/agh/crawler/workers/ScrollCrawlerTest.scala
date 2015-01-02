package pl.edu.agh.crawler.workers

import org.mockito.Matchers.anyLong
import org.mockito.Mockito._
import org.openqa.selenium.TimeoutException
import org.scalatest.{FlatSpec, Matchers}
import pl.edu.agh.crawler.browser.{Browser, BrowserWait}
import pl.edu.agh.crawler.description.{ScrollAttempt, TimeTask}

class ScrollCrawlerTest extends FlatSpec with Matchers {

  it should "have no successful scroll attempts when requesting 0" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait

    val task: TimeTask[ScrollAttempt] = new ScrollCrawler(browser, 0).execute

    verify(browser, times(0)).scrollToBottom
    task.result.successfulTimes shouldBe 0
  }

  it should "have no successful scroll attempts when DOM doesn't change" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait
    when(browserWait heightExtend (anyLong())) thenThrow new TimeoutException()

    val task: TimeTask[ScrollAttempt] = new ScrollCrawler(browser, 3).execute

    verify(browser, times(1)).scrollToBottom
    task.result.successfulTimes shouldBe 0
  }

  it should "have 1 successful scroll" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait
    doReturn(null).doThrow(classOf[TimeoutException]).when(browserWait).heightExtend(anyLong())

    val task: TimeTask[ScrollAttempt] = new ScrollCrawler(browser, 3).execute

    verify(browser, times(2)).scrollToBottom
    task.result.successfulTimes shouldBe 1
  }

  it should "have all 3 successful scrolls" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait
    doReturn(null).when(browserWait).heightExtend(anyLong())

    val task: TimeTask[ScrollAttempt] = new ScrollCrawler(browser, 3).execute

    verify(browser, times(3)).scrollToBottom
    task.result.successfulTimes shouldBe 3
  }
}
