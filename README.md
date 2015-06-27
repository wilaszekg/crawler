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
# User Agent to be used by Phantom - OPTIONAL
crawler.phantomjs.userAgent = "Chrome x.y"
# indicates if Phatom should load images
crawler.phantomjs.loadImages = false

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


# Extensions of resources to skip
crawler.excludedResources = [".css", ".jpg", ".png", ".gif"]
```

# Run the crawler
There are two ways of running the crawler. For test or development purposes a single-thread mode can be more convenient. To increase performance, use multi-thread mode.

## Single thread mode
```
import pl.edu.agh.crawler.phantom.crawlerFactory
import pl.edu.agh.crawler.task.{Crawl, SingleTask}
import pl.edu.agh.crawler.workers.Crawler

object singleThread {
  def main(args: Array[String]) {
    val crawler: Crawler = crawlerFactory createCrawler

    // crawling "https://github.com/wilaszekg/crawler" with depth 2:
    val crawlResult = crawler.crawl(SingleTask("https://github.com/wilaszekg/crawler", List(Crawl(2))))

    crawler quit
  }
}

```

## Multi thread mode
In this mode, you run a few crawlers - each on one instance of phantomJS browser. To create a multi thread crawler with 4 crawling executors, you need to create a `pl.edu.agh.crawler.concurrent.CrawlerPool` with `size` argument equal 4 and then create a new `pl.edu.agh.crawler.concurrent.MultiCrawler` using this pool:
```
val crawlerPool: CrawlerPool = new CrawlerPool(4)
val multiCrawler = new MultiCrawler(crawlerPool)
```

Then just invoke `crawl` method to crawl `url` with `depth`:
```
multiCrawler.crawl(SingleTask(url, List(Crawl(2))))
```

This call is blocking and returns as soon as there is free crawler to handle your request. This function returs a promise of crawling result: `Promise[CrawlResult]`.
To quit all crawlers of the `crawlerPool`, use `quitAll` method.

## Loading images
Loading images can be enabled in application config. If you want to override this configuration, use `loadImages` parameter when creating a new crawler:
```
val crawler = crawlerFactory.createCrawler(loadImages = false)
```

# Crawling tasks
A crawling task is description of a task to be performed by a crawler. To create a task, you have to create one of case classes of `pl.edu.agh.crawler.task.CrawlTask`:
* `SingleTask` - for crawling a single URL:
  * `url`
  * `jobs: List[Job]` - jobs to perform
  * `getPageSourceOnly` (default `false`) - forces crawler to skip all resources appart from html page source (useful for simple pages without rich interface)
  * `scrollAttempts` (default `0`) - number of attempts to scroll down the page and fetch new content
* `ComposedTask` - for crawling multiple URLs and manage cookies:
  * `tasks` - list of single tasks (class `SingleClass`)
  * `clearCookies` (default `true`) - to delete all cookies (to clear logged in sesssion)
* `ComposedAuthTask` - enriches `ComposedTask` with authentication action:
  * `composedTask` - instance of `ComposedTask`
  * `authAction` - authentication action to execute before crawling the `composedTask`
  
## Jobs
There are three types of jobs:
* `Crawl` - crawling user interface:
  * `depth` - depth of crawling user interface by interacting with html elements
* `Scroll` - scrolling down the page
  * `attempts` - how many times to try to scroll. When one attempt fails, there will be no further attempts.
* `ScreenShot` - taking screen shot


# Recording authentication actions
To authenticate a crawler in a web application, you can record your interaction with web page. You have to [download Selenium IDE](http://www.seleniumhq.org/download/) - a firefox plugin.

## How to record the action
Open Selenium IDE and start recording (red circle has to be active). Go to the log-in page and just fill in the log-in form. Wait for the page to load, go back to Selenium IDE and select File -> Export Test Case As... -> "Java / JUnit4 / WebDriver Backed". 

## How to use recorded action
Read the content of exported action and pass it to `actionRecordCompiler` object to `compile` method. It will return an instance of `ActionSupplier` which can be used as `authAction` of `ComposedAuthTask`.
```
val actionRecordCompiler = new DriverBackedActionRecordCompiler
val actionSource = Source.fromURL(getClass.getResource("/ExportedAction")).mkString
val actionSupplier = actionRecordCompiler.compile(actionSource)
```

## Alternative way to run login action
WebDriver Backed is an implementation of old deprecated Selenium interface. Unfortunately, you have to use it if you want Selenium IDE to properly export all features like wating for page to load or for element to be present. Anyway, you can export recorded behaviour as "Java / JUnit4 / WebDriver". To compile it, you will have to use another version of action record compiler:
```
val actionRecordCompiler = new ActionRecordCompiler with WebDriverTestAdapter
```
