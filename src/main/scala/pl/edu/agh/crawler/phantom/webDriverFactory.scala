package pl.edu.agh.crawler.phantom

import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
import pl.edu.agh.crawler.config.crawlerConfig

object webDriverFactory {
  def createWebDriver = {
    val capabilities = new DesiredCapabilities()
    capabilities.setCapability("phantomjs.binary.path", crawlerConfig.phantomjsPath)
    capabilities.setCapability("phantomjs.page.settings.loadImages", "false")
    capabilities.setCapability("phantomjs.cli.args", Array("--proxy-type=none"))
    val driver: PhantomJSDriver = new PhantomJSDriver(capabilities)

    driver.navigate().to("http://google.pl")
    driver
  }
}
