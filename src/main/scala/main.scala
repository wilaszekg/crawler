import java.util

import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
import pl.edu.agh.crawler.{Crawler, PageState}
import pl.edu.agh.crawler.browser.{PageElementResponse, PageElement, scripts, Browser}
import spray.json._
import DefaultJsonProtocol._
import pl.edu.agh.crawler.browser.dtoJsonProtocols._


object main {
  def getDriver = {
    val capabilities = new DesiredCapabilities()
    capabilities.setCapability("phantomjs.binary.path", "C:/devtools/phantomjs-1.9.8-windows/phantomjs.exe")
    capabilities.setCapability("phantomjs.page.settings.loadImages", "false")
    new PhantomJSDriver(capabilities)
    //new ChromeDriver()
  }

  def main(args: Array[String]) {
    val driver = getDriver

    val crawler: Crawler = new Crawler(driver, "https://www.facebook.com/WislaKrakow")
    crawler.crawl

    driver.close()
  }
}
