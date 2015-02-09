package pl.edu.agh.crawler.action

import org.mockito.Matchers.any
import org.mockito.Mockito.{mock, verify, when}
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

class actionRecordCompilerTest extends FlatSpec with Matchers {

  val behaviourSource = Source.fromURL(getClass.getResource("/Behaviour.java")).mkString

  it should "compile test class to RecorderAction" in {
    val webDriver: WebDriver = mock(classOf[WebDriver])
    val webElement = mock(classOf[WebElement])
    when(webDriver.findElement(any(classOf[By]))).thenReturn(webElement)

    val actionSupplier: ActionSupplier = actionRecordCompiler.compile(behaviourSource)
    actionSupplier(webDriver).perform()
    verify(webDriver).get("https://www.google.pl//#q=scala")
  }

  it should "give unique actions names" in {
    val action1: RecordedAction = actionRecordCompiler.compile(behaviourSource)(null)
    val action2: RecordedAction = actionRecordCompiler.compile(behaviourSource)(null)

    val name1: String = action1.getClass.getName
    val name2: String = action2.getClass.getName
    val lowerNumber = Integer.parseInt(name1 diff name2)
    val higherNumber = Integer.parseInt(name2 diff name1)

    name1 should not equal name2
    higherNumber shouldBe lowerNumber + 1
  }
}
