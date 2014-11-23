package pl.edu.agh.crawler.run

import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.description.{CrawlResult, CrawlingTask}
import pl.edu.agh.crawler.Crawler
import pl.edu.agh.crawler.phantom.webDriverFactory

object singleThread {
  def main(args: Array[String]) {
    val driver: PhantomJSDriver = webDriverFactory createWebDriver
    val crawler: Crawler = new Crawler(driver)

    //val crawlResult: CrawlResult = crawler.crawl(new CrawlingTask("https://twitter.com/WislaKrakowSA"))
    val crawlResult: CrawlResult = crawler.crawl(new CrawlingTask("https://pl-pl.facebook.com/WislaKrakow"))

    util.dump(0, crawlResult, "scroll")
  }
}
