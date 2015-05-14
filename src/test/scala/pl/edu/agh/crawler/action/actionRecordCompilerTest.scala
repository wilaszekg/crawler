package pl.edu.agh.crawler.action

import org.mockito.Matchers.any
import org.mockito.Mockito.{mock, verify, when}
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.{By, Capabilities, WebDriver, WebElement}
import org.scalatest.{Matchers, WordSpec}

import scala.io.Source

class ActionRecordCompilerTest extends WordSpec with Matchers {

  val webDriverTestSource = Source.fromURL(getClass.getResource("/WebDriverTest.java")).mkString
  val seleniumBackedTestSource = Source.fromURL(getClass.getResource("/SeleniumBacked.java")).mkString

  "ActionRecordCompiler with WebDriverTestAdapter" should {
    "compile test class to RecorderAction" in {
      val webDriver: WebDriver = mock(classOf[WebDriver])
      val webElement = mock(classOf[WebElement])
      when(webDriver.findElement(any(classOf[By]))).thenReturn(webElement)

      val actionRecordCompiler = new ActionRecordCompiler with WebDriverTestAdapter
      val actionSupplier: ActionSupplier = actionRecordCompiler.compile(webDriverTestSource)
      actionSupplier(webDriver).perform()

      verify(webDriver).get("https://www.google.pl//#q=scala")
    }

    "give unique actions names" in {
      val actionRecordCompiler = new ActionRecordCompiler with WebDriverTestAdapter

      val action1: RecordedAction = actionRecordCompiler.compile(webDriverTestSource)(null)
      val action2: RecordedAction = actionRecordCompiler.compile(webDriverTestSource)(null)

      val name1: String = action1.getClass.getName
      val name2: String = action2.getClass.getName
      val lowerNumber = Integer.parseInt(name1 diff name2)
      val higherNumber = Integer.parseInt(name2 diff name1)

      name1 should not equal name2
      higherNumber shouldBe lowerNumber + 1
    }
  }

  "ActionRecordCompiler with DriverBackedTestAdapter" should {
    "compile test class to RecorderAction" in {
      val webDriver: PhantomJSDriver = mock(classOf[PhantomJSDriver])
      val capabilities = mock(classOf[Capabilities])
      when(webDriver.getCapabilities()).thenReturn(capabilities)
      when(capabilities.isJavascriptEnabled).thenReturn(true)

      val actionRecordCompiler = new DriverBackedActionRecordCompiler
      val actionSupplier: ActionSupplier = actionRecordCompiler.compile(seleniumBackedTestSource)
      actionSupplier(webDriver).perform()

      verify(webDriver).get("http://www.site.com/")
    }
  }
}
