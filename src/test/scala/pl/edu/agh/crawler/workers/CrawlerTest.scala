package pl.edu.agh.crawler.workers

import org.mockito
import org.mockito.Mockito._
import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.scalatest.{FlatSpec, Matchers}
import pl.edu.agh.crawler.action.{ActionSupplier, RecordedAction}
import pl.edu.agh.crawler.browser.{Browser, BrowserWait}
import pl.edu.agh.crawler.result.{CollectedResult, CrawlFail, CrawlResult, SingleResult}
import pl.edu.agh.crawler.task.{ComposedTask, SingleTask}


class CrawlerTest extends FlatSpec with Matchers {

  class TestCrawler extends Crawler(mock(classOf[PhantomJSDriver])) {
    override val browser = mock(classOf[Browser])

    doReturn(browserWait).when(browser).waitUntil
  }

  val browserWait = mock(classOf[BrowserWait])

  it should "crawl with depth 1" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl SingleTask("any-url", 1)

    verify(crawler.browser, times(1)).executeCrawlingScripts
  }

  it should "crawl with depth 3" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl SingleTask("any-url", 3)

    verify(crawler.browser, times(3)).executeCrawlingScripts
  }

  it should "cleanup browser" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl SingleTask("any-url", 1)

    verify(crawler.browser, times(1)).cleanUp
  }

  it should "cleanup prepare custom scripts" in {
    val crawler: Crawler = new TestCrawler

    crawler crawl SingleTask("any-url", 1)

    verify(crawler.browser, times(1)).prepareCustomScripts
  }

  it should "crawl single task" in {
    val crawler = new TestCrawler
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
    val crawler = new TestCrawler
    val (actionSupplier: ActionSupplier, action: RecordedAction) = mockAuthAction

    crawler crawl ComposedTask(List(), actionSupplier)

    verify(action).perform()
  }


  it should "return CollectedResult for ComposedTask" in {
    val crawler = new TestCrawler
    val (actionSupplier: ActionSupplier, action: RecordedAction) = mockAuthAction

    val result: CrawlResult = crawler crawl ComposedTask(List(), actionSupplier)

    result shouldBe a[CollectedResult]
  }

  it should "crawl composed tasks" in {
    val crawler = new TestCrawler
    val (actionSupplier, action) = mockAuthAction
    doReturn("source1").doReturn("source2").when(crawler.driver).getPageSource
    val task1: SingleTask = SingleTask("first.url", 1)
    val task2: SingleTask = SingleTask("second.url", 1)

    val result: CrawlResult = crawler crawl ComposedTask(List(task1, task2), actionSupplier)

    result match {
      case SingleResult(_, _, _) => fail("expected CollectedResult - was SingleResult")
      case CollectedResult(results) => {
        results.map(_.asInstanceOf[SingleResult].task) should contain inOrderOnly(task1, task2)
        results.map(_.asInstanceOf[SingleResult].crawledContent.pageContent) should contain inOrderOnly("source1", "source2")
      }
    }
  }

  it should "wrap any exception into CrawlFail" in {
    val crawler = new TestCrawler
    val (actionSupplier, action) = mockAuthAction
    doThrow(classOf[IllegalStateException]).doReturn("source2").when(crawler.driver).getPageSource
    val task1: SingleTask = SingleTask("first.url", 1)

    val result: CrawlResult = crawler crawl ComposedTask(List(task1, SingleTask("second.url", 1)), actionSupplier)

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
