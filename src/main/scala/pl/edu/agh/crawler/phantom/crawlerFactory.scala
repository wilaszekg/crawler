package pl.edu.agh.crawler.phantom

import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities
import pl.edu.agh.crawler.config.crawlerConfig
import pl.edu.agh.crawler.workers.Crawler

object crawlerFactory {

  def createCrawler = new Crawler(createWebDriver)

  private def createWebDriver = {
    val capabilities = DesiredCapabilities.phantomjs()
    capabilities.setCapability("phantomjs.binary.path", crawlerConfig.phantomjsPath)
    capabilities.setCapability("phantomjs.page.settings.loadImages", crawlerConfig.phantomLoadImages.toString)
    capabilities.setCapability("phantomjs.cli.args", Array("--proxy-type=none"))
    crawlerConfig.userAgent.foreach {
      capabilities.setCapability("phantomjs.page.settings.userAgent", _)
    }
    val driver = new PhantomJSDriver(capabilities)
    driver.executePhantomJS("this.viewportSize = {width: 1280,height: 800};")
    driver
  }
}
