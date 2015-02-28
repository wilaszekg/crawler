package pl.edu.agh.crawler.workers

import org.mockito
import org.mockito.Mockito._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.scalatest.{FlatSpec, Matchers}
import pl.edu.agh.crawler.action.{ActionSupplier, RecordedAction}
import pl.edu.agh.crawler.browser.{Browser, BrowserWait}
import pl.edu.agh.crawler.result.{CollectedResult, CrawlFail, CrawlResult, SingleResult}
import pl.edu.agh.crawler.task.{ComposedAuthTask, ComposedTask, SingleTask}


class CrawlerTest extends FlatSpec with Matchers {

  def testCrawler() = {
    val browser = mock(classOf[Browser])
    doReturn(browserWait).when(browser).waitUntil

    new Crawler(browser, mock(classOf[PhantomJSDriver]))
  }

  val browserWait = mock(classOf[BrowserWait])

  it should "prepare phantom webPage on crawler init" in {
    val crawler: Crawler = testCrawler()

    verify(crawler.browser).preparePhantomWebPage
  }

  it should "crawl with depth 1" in {
    val crawler: Crawler = testCrawler()

    crawler crawl SingleTask("any-url", 1)

    verify(crawler.browser, times(1)).executeCrawlingScripts
  }

  it should "crawl with depth 3" in {
    val crawler: Crawler = testCrawler()

    crawler crawl SingleTask("any-url", 3)

    verify(crawler.browser, times(3)).executeCrawlingScripts
  }

  it should "cleanup browser" in {
    val crawler: Crawler = testCrawler()

    crawler crawl SingleTask("any-url", 1)

    verify(crawler.browser, times(1)).cleanUp
  }

  it should "cleanup prepare custom scripts" in {
    val crawler: Crawler = testCrawler()

    crawler crawl SingleTask("any-url", 1)

    verify(crawler.browser, times(1)).prepareCustomScripts
  }

  it should "crawl single task" in {
    val crawler = testCrawler()
    when(crawler.driver.getPageSource).thenReturn("page source")
    val task: SingleTask = SingleTask("any.url", 0)

    val result: CrawlResult = crawler crawl task

    result shouldBe a[SingleResult]
    result match {
      case SingleResult(resultTask, content, _) => {
        resultTask shouldBe task
        content.pageContent shouldBe "page source"
      }
    }
  }

  it should "run auth action" in {
    val crawler = testCrawler()
    val (actionSupplier: ActionSupplier, action: RecordedAction) = mockAuthAction

    crawler crawl ComposedAuthTask(ComposedTask(List()), actionSupplier)

    verify(action).perform()
  }

  it should "return CollectedResult for ComposedTask" in {
    val crawler = testCrawler()

    val result: CrawlResult = crawler crawl ComposedTask(List())

    result shouldBe a[CollectedResult]
  }

  it should "return CollectedResult for ComposedAuthTask" in {
    val crawler = testCrawler()
    val (actionSupplier: ActionSupplier, action: RecordedAction) = mockAuthAction

    val result = crawler crawl ComposedAuthTask(ComposedTask(List()), actionSupplier)

    result shouldBe a[CollectedResult]
  }

  it should "not clear cookies" in {
    val crawler = testCrawler()

    crawler crawl ComposedTask(List(), clearCookies = false)

    verify(crawler.browser, never()).deleteAllCookies
  }

  it should "crawl composed tasks" in {
    val crawler = testCrawler()
    doReturn("source1").doReturn("source2").when(crawler.driver).getPageSource
    val task1: SingleTask = SingleTask("first.url", 1)
    val task2: SingleTask = SingleTask("second.url", 1)

    val result: CrawlResult = crawler crawl ComposedTask(List(task1, task2))

    result match {
      case SingleResult(_, _, _) => fail("expected CollectedResult - was SingleResult")
      case CollectedResult(results) => {
        results.map(_.asInstanceOf[SingleResult].task) should contain inOrderOnly(task1, task2)
        results.map(_.asInstanceOf[SingleResult].crawledContent.pageContent) should contain inOrderOnly("source1", "source2")
      }
    }
  }

  it should "wrap any exception into CrawlFail" in {
    val crawler = testCrawler()
    doThrow(classOf[IllegalStateException]).doReturn("source2").when(crawler.driver).getPageSource
    val task1: SingleTask = SingleTask("first.url", 1)

    val result: CrawlResult = crawler crawl ComposedTask(List(task1, SingleTask("second.url", 1)))

    result match {
      case CollectedResult(results) => {
        results.head match {
          case CrawlFail(task, cause) => {
            task shouldBe task1
            cause shouldBe a[IllegalStateException]
          }
        }
      }
    }

  }

  def mockAuthAction: (ActionSupplier, RecordedAction) = {
    val actionSupplier: ActionSupplier = mock(classOf[ActionSupplier])
    val action = mock(classOf[RecordedAction])
    when(actionSupplier.apply(mockito.Matchers.any(classOf[WebDriver]))).thenReturn(action)
    (actionSupplier, action)
  }

}
