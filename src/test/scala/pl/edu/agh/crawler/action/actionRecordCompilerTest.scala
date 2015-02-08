package pl.edu.agh.crawler.action

import org.mockito.Mockito.{mock, when}
import org.mockito.{Matchers, Mockito}
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.FlatSpec

import scala.io.Source

class actionRecordCompilerTest extends FlatSpec {

  val behaviourSource = Source.fromURL(getClass.getResource("/Behaviour.java")).mkString

  it should "execute original test method" in {
    val webDriver: WebDriver = mock(classOf[WebDriver])
    val webElement = mock(classOf[WebElement])
    when(webDriver.findElement(Matchers.any(classOf[By]))).thenReturn(webElement)

    val actionSupplier: ActionSupplier = actionRecordCompiler.compile(behaviourSource)
    actionSupplier(webDriver).perform()
    Mockito.verify(webDriver).get("https://www.google.pl//#q=scala")
  }

}
