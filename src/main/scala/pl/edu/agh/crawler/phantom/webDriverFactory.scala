package pl.edu.agh.crawler.phantom

import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities

object webDriverFactory {
  def createWebDriver = {
    val capabilities = new DesiredCapabilities()
    capabilities.setCapability("phantomjs.binary.path", "C:/devtools/phantomjs-1.9.8-windows/phantomjs.exe")
    capabilities.setCapability("phantomjs.page.settings.loadImages", "false")
    capabilities.setCapability("phantomjs.cli.args", Array("--proxy-type=none"))
    val driver: PhantomJSDriver = new PhantomJSDriver(capabilities)

    driver.navigate().to("http://google.pl")
    driver
  }
}
