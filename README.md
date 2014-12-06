crawler
=======

Web crawler executing JavaScript

# Install
1. [Download PhantomJS](http://phantomjs.org/download.html)
2. You need scala and SBT
3. Add the crawler to your SBT dependencies

# Configuration
The crawler is using [typesafe configs](https://github.com/typesafehub/config), so you need to provide configuration for the crawler in your `application.conf`. This is en example configuration:
```
# your phantomJS installation path
crawler.phantomjs.path = "C:/tools/phantomjs-1.9.8-windows/phantomjs.exe"

# Timeouts - in seconds

# Load full page in the browser
crawler.timeout.pageLoad = 10
# Complete all ajax requests after executing crawling scripts
crawler.timeout.ajaxComplete = 5
# Detect when no more changes are being performend on the HTML structure
crawler.timeout.domStable = 5

# Complete all ajax requests after scrolling down the page
crawler.timeout.scrollEffectAjax = 3
# Detect when some more content was added to the HTML after scrolling
crawler.timeout.scrollEffectRender = 3
```

# Run the crawler
There are two ways of running the crawler. For test or development purposes a single-thread mode can be more convenient. To increase performance, use multi-thread mode.

## Single thread mode
```
import org.openqa.selenium.phantomjs.PhantomJSDriver
import pl.edu.agh.crawler.description.{CrawlResult, CrawlingTask}
import pl.edu.agh.crawler.phantom.webDriverFactory
import pl.edu.agh.crawler.workers.Crawler

object singleThread {
  def main(args: Array[String]) {
    val driver: PhantomJSDriver = webDriverFactory createWebDriver
    val crawler: Crawler = new Crawler(driver)

    // crawling "https://github.com/wilaszekg/crawler" with 3 attempts to scroll down the page
    val crawlResult: CrawlResult = crawler.crawl(new CrawlingTask("https://github.com/wilaszekg/crawler", 3))

    driver.quit()
  }
}

```

## Multi thread mode
In this mode, you run a few crawlers - each on one instance of phantomJS browser. To create a multi thread crawler with 4 crawling executors, you need to create a `pl.edu.agh.crawler.api.CrawlerPool` with `size` argument equal 4 and then create a new `pl.edu.agh.crawler.api.MultiCrawler` using this pool:
```
val crawlerPool: CrawlerPool = new CrawlerPool(4)
val multiCrawler = new MultiCrawler(crawlerPool)
```

Then just invoke `crawl` method to crawl `url` with `scrollAttempts`:
```
multiCrawler.crawl(new CrawlingTask(url, scrollAttempts))
```

This call is blocking and returns as soon as there is free crawler to handle your request. This function returs a promise of crawling result: `Promise[CrawlResult]`
