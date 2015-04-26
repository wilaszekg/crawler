package pl.edu.agh.crawler.workers

import org.mockito.Matchers.anyLong
import org.mockito.Mockito._
import org.openqa.selenium.TimeoutException
import org.scalatest.{FlatSpec, Matchers}
import pl.edu.agh.crawler.browser.{Browser, BrowserWait}

class ScrollCrawlerTest extends FlatSpec with Matchers {

  it should "have no successful scroll attempts when requesting 0" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait

    val result = new ScrollCrawler(browser).execute(0)

    verify(browser, times(0)).scrollToBottom
    result.successAttempts shouldBe 0
  }

  it should "have no successful scroll attempts when DOM doesn't change" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait
    when(browserWait heightExtend (anyLong())) thenThrow new TimeoutException()

    val result = new ScrollCrawler(browser).execute(3)

    verify(browser, times(1)).scrollToBottom
    result.successAttempts shouldBe 0
  }

  it should "have 1 successful scroll" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait
    doReturn(null).doThrow(classOf[TimeoutException]).when(browserWait).heightExtend(anyLong())

    val result = new ScrollCrawler(browser).execute(3)

    verify(browser, times(2)).scrollToBottom
    result.successAttempts shouldBe 1
  }

  it should "have all 3 successful scrolls" in {
    val browser: Browser = mock(classOf[Browser])
    val browserWait = mock(classOf[BrowserWait])
    when(browser waitUntil) thenReturn browserWait
    doReturn(null).when(browserWait).heightExtend(anyLong())

    val result = new ScrollCrawler(browser).execute(3)

    verify(browser, times(3)).scrollToBottom
    result.successAttempts shouldBe 3
  }
}
