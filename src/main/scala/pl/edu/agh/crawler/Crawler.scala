package pl.edu.agh.crawler

import java.io.{File, PrintWriter}

import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.browser.Browser

class Crawler(val driver: PhantomJSDriver, val url: String) {

  def crawl = {
    driver.navigate().to("http://google.com")
    val browser: Browser = new Browser(driver)
    val start = System.currentTimeMillis()
    browser.goTo(url)
    val end = System.currentTimeMillis()
    println("Time: " + (end - start))

    browser.prepareCustomScripts
    browser.executeCrawlingScripts

    browser.waitUntilAjaxCompleted

    Thread.sleep(1000)
    val removedText = browser.getRemovedText

    println(removedText)
    val writer: PrintWriter = new PrintWriter(new File("crawled.html"))
    writer.write(driver.getPageSource)

  }

}
