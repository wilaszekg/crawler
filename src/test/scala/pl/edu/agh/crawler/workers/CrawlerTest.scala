package pl.edu.agh.crawler.workers

import org.mockito.Mockito.{doReturn, mock, times, verify}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.scalatest.FlatSpec
import pl.edu.agh.crawler.browser.{Browser, BrowserWait}
import pl.edu.agh.crawler.description.CrawlingTask


class CrawlerTest extends FlatSpec {

  class TestCrawler extends Crawler(mock(classOf[PhantomJSDriver])) {
    override val browser = mock(classOf[Browser])

    doReturn(browserWait).when(browser).waitUntil
  }

  val browserWait = mock(classOf[BrowserWait])

  it should "crawl with depth 1" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl new CrawlingTask("any-url", 1)

    verify(crawler.browser, times(1)).executeCrawlingScripts
  }

  it should "crawl with depth 3" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl new CrawlingTask("any-url", 3)

    verify(crawler.browser, times(3)).executeCrawlingScripts
  }

  it should "cleanup browser" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl new CrawlingTask("any-url", 1)

    verify(crawler.browser, times(1)).cleanUp
  }

  it should "cleanup prepare custom scripts" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl new CrawlingTask("any-url", 1)

    verify(crawler.browser, times(1)).prepareCustomScripts
  }

}
