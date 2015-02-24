package pl.edu.agh.crawler.browser

import org.mockito.Mockito
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.scalatest.FlatSpec

class BrowserTest extends FlatSpec {

  it should "execute custom scripts" in {
    val driver = Mockito.mock(classOf[PhantomJSDriver])
    val browser = new Browser(driver)

    browser.prepareCustomScripts

    Mockito.verify(driver).executeScript(scripts.util)
    Mockito.verify(driver).executeScript(scripts.domEvents)
  }
}
